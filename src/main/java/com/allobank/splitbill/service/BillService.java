package com.allobank.splitbill.service;

import com.allobank.splitbill.api.dto.request.CreateExpenseRequest;
import com.allobank.splitbill.api.dto.request.CreateGroupRequest;
import com.allobank.splitbill.api.dto.request.ShareInput;
import com.allobank.splitbill.api.dto.response.BalanceResponse;
import com.allobank.splitbill.api.dto.response.ExpenseResponse;
import com.allobank.splitbill.api.dto.response.GroupResponse;
import com.allobank.splitbill.api.dto.response.ParticipantResponse;
import com.allobank.splitbill.api.dto.response.SettlementResponse;
import com.allobank.splitbill.api.dto.response.ShareResponse;
import com.allobank.splitbill.api.dto.response.TransferResponse;
import com.allobank.splitbill.domain.ExpenseCalculator;
import com.allobank.splitbill.domain.ExpenseCalculator.ShareAllocation;
import com.allobank.splitbill.domain.BillStatus;
import com.allobank.splitbill.domain.ServiceCharge;
import com.allobank.splitbill.domain.SplitType;
import com.allobank.splitbill.domain.SplitShare;
import com.allobank.splitbill.domain.TaxMode;
import com.allobank.splitbill.domain.Transfer;
import com.allobank.splitbill.persistence.BillRepository;
import com.allobank.splitbill.persistence.BillRepository.ExpenseRow;
import com.allobank.splitbill.persistence.BillRepository.GroupRow;
import com.allobank.splitbill.persistence.BillRepository.ParticipantRow;
import com.allobank.splitbill.persistence.BillRepository.ShareRow;
import com.allobank.splitbill.persistence.BillRepository.UserRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BillService {
    private static final BigDecimal ZERO = new BigDecimal("0.00");

    private final BillRepository repository;
    private final SettlementService settlementService;
    private final ExpenseCalculator expenseCalculator = new ExpenseCalculator();
    private final ServiceCharge serviceCharge;

    public BillService(BillRepository repository, SettlementService settlementService,
                       ServiceCharge serviceCharge) {
        this.repository = repository;
        this.settlementService = settlementService;
        this.serviceCharge = serviceCharge;
    }

    @Transactional
    public GroupResponse createGroup(CreateGroupRequest request) {
        if (request == null || request.name() == null || request.participants() == null
                || request.participants().size() < 2 || request.participants().size() > 100) {
            throw new IllegalArgumentException("a group needs a name and 2 to 100 participants");
        }
        String groupName = request.name().trim();
        if (groupName.isEmpty() || groupName.length() > 120) {
            throw new IllegalArgumentException("group name must contain 1 to 120 characters");
        }
        String currency = normalizeCurrency(request.currency());

        Set<String> seenUsernames = new HashSet<>();
        List<UserRow> users = new ArrayList<>();
        for (String value : request.participants()) {
            String username = UsernamePolicy.normalize(value);
            if (!seenUsernames.add(username)) {
                throw new IllegalArgumentException("participant usernames must be unique within a group");
            }
            UserRow user = repository.findUserByUsername(username)
                    .orElseThrow(() -> new NotFoundException("user " + UsernamePolicy.handle(username) + " not found"));
            users.add(user);
        }

        GroupRow group = new GroupRow(UUID.randomUUID(), groupName, currency, Instant.now());
        repository.insertGroup(group);
        List<ParticipantResponse> participants = new ArrayList<>();
        for (int index = 0; index < users.size(); index++) {
            UserRow user = users.get(index);
            ParticipantRow participant = new ParticipantRow(UUID.randomUUID(), group.id(), user.id(),
                    user.username(), user.displayName(), index);
            repository.insertParticipant(participant);
            participants.add(toParticipantResponse(participant));
        }
        return new GroupResponse(group.id(), group.name(), group.currency(),
                List.copyOf(participants), group.createdAt());
    }

    @Transactional(readOnly = true)
    public GroupResponse getGroup(UUID groupId) {
        GroupRow group = requireGroup(groupId);
        List<ParticipantResponse> participants = repository.findParticipants(groupId).stream()
                .map(this::toParticipantResponse)
                .toList();
        return new GroupResponse(group.id(), group.name(), group.currency(), participants, group.createdAt());
    }

    @Transactional
    public ExpenseResponse createExpense(UUID groupId, CreateExpenseRequest request) {
        requireGroup(groupId);
        if (request == null || request.description() == null || request.description().isBlank()
                || request.description().trim().length() > 240 || request.paidBy() == null) {
            throw new IllegalArgumentException("description and paid_by are required");
        }
        List<ParticipantRow> participants = repository.findParticipants(groupId);
        Map<String, ParticipantRow> participantsByUsername = participants.stream()
                .collect(Collectors.toMap(
                        participant -> UsernamePolicy.normalize(participant.username()),
                        Function.identity()));
        UUID paidBy = requireGroupParticipant(request.paidBy(), "paid_by", participantsByUsername);
        List<UUID> participantIds = participants.stream().map(ParticipantRow::id).toList();
        TaxMode taxMode = request.tax() == null ? TaxMode.NONE : request.tax().mode();
        BigDecimal taxRatePct = request.tax() == null ? null : request.tax().ratePct();
        if (request.splitBetween() != null && !request.splitBetween().isEmpty()
                && request.splitType() != SplitType.EQUAL) {
            throw new IllegalArgumentException("split_between can only be used with EQUAL split_type");
        }
        if (request.splitBetween() != null && !request.splitBetween().isEmpty()
                && request.shares() != null && !request.shares().isEmpty()) {
            throw new IllegalArgumentException("use either split_between or shares, not both");
        }
        List<ShareInput> requestedShareInputs = request.shares();
        if (request.splitBetween() != null && !request.splitBetween().isEmpty()) {
            requestedShareInputs = request.splitBetween().stream()
                    .map(username -> new ShareInput(username, null, null))
                    .toList();
        }
        List<SplitShare> requestedShares = requestedShareInputs == null ? null : requestedShareInputs.stream()
                .map(share -> new SplitShare(
                        requireGroupParticipant(share.username(), "shares username", participantsByUsername),
                        share.amount(), share.percentage()))
                .toList();
        ExpenseCalculator.Result calculation = expenseCalculator.calculate(request.amount(), taxMode,
                taxRatePct, request.splitType(), participantIds, requestedShares);
        ExpenseRow expense = new ExpenseRow(UUID.randomUUID(), groupId, request.description().trim(),
                calculation.totals().inputAmount(), calculation.totals().mode(),
                calculation.totals().ratePct(), calculation.totals().subtotal(),
                calculation.totals().taxAmount(), calculation.totals().totalAmount(),
                paidBy, request.splitType(), Instant.now());
        List<ShareRow> shares = calculation.shares().entrySet().stream()
                .map(entry -> new ShareRow(expense.id(), entry.getKey(), entry.getValue().subtotal(),
                        entry.getValue().taxAmount(), entry.getValue().amount()))
                .toList();
        repository.insertExpense(expense, shares);
        return toExpenseResponse(expense, calculation.shares());
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpenses(UUID groupId) {
        requireGroup(groupId);
        Map<UUID, Map<UUID, ShareAllocation>> sharesByExpense = settlementService.sharesByExpense(groupId);
        return repository.findExpenses(groupId).stream()
                .map(expense -> toExpenseResponse(expense,
                        sharesByExpense.getOrDefault(expense.id(), Map.of())))
                .toList();
    }

    @Transactional(readOnly = true)
    public ExpenseResponse getExpense(UUID groupId, UUID expenseId) {
        requireGroup(groupId);
        ExpenseRow expense = repository.findExpense(groupId, expenseId)
                .orElseThrow(() -> new NotFoundException("expense not found"));
        Map<UUID, ShareAllocation> shares = settlementService.sharesByExpense(groupId)
                .getOrDefault(expenseId, Map.of());
        return toExpenseResponse(expense, shares);
    }

    @Transactional(readOnly = true)
    public SettlementResponse getSettlement(UUID groupId) {
        SettlementService.GroupSettlement settlement = settlementService.calculate(groupId);
        Map<UUID, String> usernamesByParticipantId = settlement.participants().stream()
                .collect(Collectors.toMap(ParticipantRow::id,
                        participant -> UsernamePolicy.handle(participant.username())));
        List<BalanceResponse> balances = settlement.participants().stream()
                .map(participant -> {
                    BigDecimal balance = settlement.balances().get(participant.id());
                    BigDecimal youOwe = balance.signum() < 0 ? balance.negate() : ZERO;
                    BigDecimal youAreOwed = balance.signum() > 0 ? balance : ZERO;
                    BillStatus status = balance.signum() < 0 ? BillStatus.YOU_OWE
                            : balance.signum() > 0 ? BillStatus.YOU_ARE_OWED : BillStatus.SETTLED;
                    return new BalanceResponse(participant.id(),
                            UsernamePolicy.handle(participant.username()), participant.displayName(),
                            balance, youOwe, youAreOwed, status);
                })
                .toList();
        return new SettlementResponse(groupId, settlement.group().currency(),
                settlement.totalSubtotal(), settlement.totalTax(), settlement.totalExpenses(),
                serviceCharge.percentage(), serviceCharge.amountFor(settlement.totalExpenses()), balances,
                settlement.transfers().stream()
                        .map(transfer -> toTransferResponse(transfer, usernamesByParticipantId)).toList());
    }

    private GroupRow requireGroup(UUID groupId) {
        return repository.findGroup(groupId)
                .orElseThrow(() -> new NotFoundException("group not found"));
    }

    private UUID requireGroupParticipant(String username, String field,
                                         Map<String, ParticipantRow> participantsByUsername) {
        String normalizedUsername = UsernamePolicy.normalize(username);
        ParticipantRow participant = participantsByUsername.get(normalizedUsername);
        if (participant == null) {
            throw new IllegalArgumentException(field + " must be a participant in this group");
        }
        return participant.id();
    }

    private ParticipantResponse toParticipantResponse(ParticipantRow participant) {
        return new ParticipantResponse(participant.id(), participant.userId(),
                UsernamePolicy.handle(participant.username()), participant.displayName());
    }

    private ExpenseResponse toExpenseResponse(ExpenseRow expense, Map<UUID, ShareAllocation> shares) {
        List<ShareResponse> responseShares = shares.entrySet().stream()
                .map(entry -> new ShareResponse(entry.getKey(), entry.getValue().subtotal(),
                        entry.getValue().taxAmount(), entry.getValue().amount()))
                .toList();
        return new ExpenseResponse(expense.id(), expense.groupId(), expense.description(),
                expense.inputAmount(), expense.taxMode(), expense.taxRatePct(), expense.subtotal(),
                expense.taxAmount(), expense.amount(), expense.paidBy(), expense.splitType(),
                responseShares, expense.createdAt());
    }

    private TransferResponse toTransferResponse(Transfer transfer, Map<UUID, String> usernamesByParticipantId) {
        return new TransferResponse(transfer.from(), transfer.to(), transfer.amount(),
                usernamesByParticipantId.get(transfer.from()), usernamesByParticipantId.get(transfer.to()));
    }

    private String normalizeCurrency(String value) {
        String currency = value == null || value.isBlank() ? "IDR" : value.trim().toUpperCase(Locale.ROOT);
        if (!currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("currency must be a three-letter ISO code");
        }
        return currency;
    }
}
