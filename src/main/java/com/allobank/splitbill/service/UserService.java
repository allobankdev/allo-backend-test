package com.allobank.splitbill.service;

import com.allobank.splitbill.api.dto.request.CreateUserRequest;
import com.allobank.splitbill.api.dto.response.CurrencyBillSummaryResponse;
import com.allobank.splitbill.api.dto.response.GroupBillSummaryResponse;
import com.allobank.splitbill.api.dto.response.PersonalBillsResponse;
import com.allobank.splitbill.api.dto.response.TransferResponse;
import com.allobank.splitbill.api.dto.response.UserResponse;
import com.allobank.splitbill.domain.BillStatus;
import com.allobank.splitbill.domain.Transfer;
import com.allobank.splitbill.persistence.BillRepository;
import com.allobank.splitbill.persistence.BillRepository.GroupRow;
import com.allobank.splitbill.persistence.BillRepository.ParticipantRow;
import com.allobank.splitbill.persistence.BillRepository.UserRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final BigDecimal ZERO = new BigDecimal("0.00");

    private final BillRepository repository;
    private final SettlementService settlementService;

    public UserService(BillRepository repository, SettlementService settlementService) {
        this.repository = repository;
        this.settlementService = settlementService;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("user request is required");
        }
        String username = UsernamePolicy.normalize(request.username());
        String displayName = normalizeDisplayName(request.displayName());
        if (repository.findUserByUsername(username).isPresent()) {
            throw new ConflictException("username " + UsernamePolicy.handle(username) + " already exists");
        }
        UserRow user = new UserRow(UUID.randomUUID(), username, displayName, Instant.now());
        repository.insertUser(user);
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(String username) {
        return toResponse(requireUser(username));
    }

    @Transactional(readOnly = true)
    public PersonalBillsResponse getPersonalBills(String username) {
        UserRow user = requireUser(username);
        List<GroupBillSummaryResponse> groupSummaries = new ArrayList<>();
        Map<String, CurrencyAccumulator> totals = new TreeMap<>();

        for (GroupRow group : repository.findGroupsForUser(user.id())) {
            ParticipantRow membership = repository.findParticipantForUser(group.id(), user.id())
                    .orElseThrow(() -> new IllegalStateException("group membership is inconsistent"));
            SettlementService.GroupSettlement settlement = settlementService.calculate(group.id());
            BigDecimal balance = settlement.balances().getOrDefault(membership.id(), ZERO);
            BigDecimal youOwe = balance.signum() < 0 ? balance.negate() : ZERO;
            BigDecimal youAreOwed = balance.signum() > 0 ? balance : ZERO;
            BillStatus status = balance.signum() < 0 ? BillStatus.YOU_OWE
                    : balance.signum() > 0 ? BillStatus.YOU_ARE_OWED : BillStatus.SETTLED;
            Map<UUID, String> usernamesByParticipantId = settlement.participants().stream()
                    .collect(Collectors.toMap(ParticipantRow::id,
                            participant -> UsernamePolicy.handle(participant.username())));
            List<TransferResponse> transfers = settlement.transfers().stream()
                    .filter(transfer -> transfer.from().equals(membership.id())
                            || transfer.to().equals(membership.id()))
                    .map(transfer -> toTransferResponse(transfer, usernamesByParticipantId))
                    .toList();
            groupSummaries.add(new GroupBillSummaryResponse(group.id(), group.name(), group.currency(),
                    membership.id(), UsernamePolicy.handle(membership.username()), balance, youOwe, youAreOwed, status,
                    settlement.totalExpenses(), transfers));
            totals.computeIfAbsent(group.currency(), ignored -> new CurrencyAccumulator())
                    .add(balance, youOwe, youAreOwed);
        }

        List<CurrencyBillSummaryResponse> currencySummaries = totals.entrySet().stream()
                .map(entry -> new CurrencyBillSummaryResponse(entry.getKey(), entry.getValue().youOwe,
                        entry.getValue().youAreOwed, entry.getValue().netBalance))
                .toList();
        return new PersonalBillsResponse(toResponse(user), currencySummaries, List.copyOf(groupSummaries));
    }

    private UserRow requireUser(String value) {
        String username = UsernamePolicy.normalize(value);
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException("user not found"));
    }

    private UserResponse toResponse(UserRow user) {
        return new UserResponse(user.id(), UsernamePolicy.handle(user.username()),
                user.displayName(), user.createdAt());
    }

    private TransferResponse toTransferResponse(Transfer transfer, Map<UUID, String> usernamesByParticipantId) {
        return new TransferResponse(transfer.from(), transfer.to(), transfer.amount(),
                usernamesByParticipantId.get(transfer.from()), usernamesByParticipantId.get(transfer.to()));
    }

    private String normalizeDisplayName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("display_name is required");
        }
        String displayName = value.trim();
        if (displayName.length() > 120) {
            throw new IllegalArgumentException("display_name must contain 1 to 120 characters");
        }
        return displayName;
    }

    private static final class CurrencyAccumulator {
        private BigDecimal youOwe = ZERO;
        private BigDecimal youAreOwed = ZERO;
        private BigDecimal netBalance = ZERO;

        private void add(BigDecimal balance, BigDecimal owed, BigDecimal receivable) {
            youOwe = youOwe.add(owed);
            youAreOwed = youAreOwed.add(receivable);
            netBalance = netBalance.add(balance);
        }
    }
}
