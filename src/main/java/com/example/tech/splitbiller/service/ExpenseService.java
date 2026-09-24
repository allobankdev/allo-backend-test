package com.example.tech.splitbiller.service;

import com.example.tech.splitbiller.entity.Expense;
import com.example.tech.splitbiller.entity.ExpenseCategory;
import com.example.tech.splitbiller.entity.ExpenseShare;
import com.example.tech.splitbiller.entity.Group;
import com.example.tech.splitbiller.entity.IdempotencyKey;
import com.example.tech.splitbiller.entity.Person;
import com.example.tech.splitbiller.exception.DataInvalidException;
import com.example.tech.splitbiller.exception.DataNotFoundException;
import com.example.tech.splitbiller.exception.TransactionConflictException;
import com.example.tech.splitbiller.mapper.ExpenseMapper;
import com.example.tech.splitbiller.model.SplitParticipant;
import com.example.tech.splitbiller.model.request.CreateExpenseRequest;
import com.example.tech.splitbiller.model.request.SplitParticipantRequest;
import com.example.tech.splitbiller.model.response.ExpenseCategorySummaryResponse;
import com.example.tech.splitbiller.model.response.ExpenseResponse;
import com.example.tech.splitbiller.repository.ExpenseRepository;
import com.example.tech.splitbiller.util.SplitStrategy;
import com.example.tech.splitbiller.util.SplitStrategyFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final GroupService groupService;
    private final PersonService personService;
    private final ExpenseCategoryService expenseCategoryService;
    private final IdempotencyKeyService idempotencyKeyService;
    private final GroupMemberService groupMemberService;
    private final ObjectMapper objectMapper;
    private final ExpenseMapper expenseMapper;
    private final SplitStrategyFactory strategyFactory;


    public ExpenseService(ExpenseRepository expenseRepository,
                          GroupService groupService,
                          PersonService personService,
                          ExpenseCategoryService expenseCategoryService,
                          IdempotencyKeyService idempotencyKeyService,
                          GroupMemberService groupMemberService,
                          ObjectMapper objectMapper,
                          ExpenseMapper expenseMapper,
                          SplitStrategyFactory strategyFactory) {
        this.expenseRepository = expenseRepository;
        this.groupService = groupService;
        this.personService = personService;
        this.expenseCategoryService = expenseCategoryService;
        this.idempotencyKeyService = idempotencyKeyService;
        this.groupMemberService = groupMemberService;
        this.objectMapper = objectMapper;
        this.expenseMapper = expenseMapper;
        this.strategyFactory = strategyFactory;
    }

    public ExpenseResponse createExpense(String idempotencyKey, CreateExpenseRequest request) {
        String hashedRequest = objectMapper.writeValueAsString(request);
        Optional<IdempotencyKey> find = idempotencyKeyService.findByIdempotencyKey(idempotencyKey);
        if(find.isPresent()) {
            IdempotencyKey idempotencyKeyObj = find.get();
            if(!idempotencyKeyObj.getHashRequest().equals(hashedRequest))
                throw new TransactionConflictException("Idempotency-Key was already used with a different request");

            String response = idempotencyKeyObj.getResponse();
            return objectMapper.readValue(response, ExpenseResponse.class);
        }

        ExpenseCategory category = expenseCategoryService.getByIdAndDeleted(request.categoryId());

        validateNoDuplicateParticipants(request.split().shares());

        Group group = groupService.getActiveById(request.groupId());
        Person payer = personService.getById(request.paidBy());
        groupMemberService.validateMember(group, payer);

        List<SplitParticipant> participants = resolveParticipants(group, request.split().shares());
        SplitStrategy strategy = strategyFactory.get(request.split().type());

        long epochNow = Instant.now().toEpochMilli();
        Expense expense = new Expense();
        expense.setDescription(request.description());
        expense.setAmount(request.amount());
        expense.setGroup(group);
        expense.setPaidBy(payer);
        expense.setExpenseCategory(category);
        expense.setSplitType(request.split().type());
        expense.setCreatedAt(epochNow);

        List<ExpenseShare> shares = strategy.calculate(expense, participants);
        validateTotal(request.amount(), shares);
        expense.setShares(shares);
        Expense saved = expenseRepository.save(expense);

        ExpenseResponse response = expenseMapper.toExpenseResponse(saved);
        idempotencyKeyService.save(idempotencyKey, hashedRequest, objectMapper.writeValueAsString(response));
        return response;
    }

    public List<ExpenseCategorySummaryResponse> summarizeByCategoryAndGroup(String groupId) {
        return expenseRepository.summarizeByCategoryAndGroup(groupId);
    }

    public List<ExpenseCategorySummaryResponse> summarizeByCategory() {
        return expenseRepository.summarizeByCategory();
    }

    public List<ExpenseResponse> getAllForBalanceCalculation(Group group) {
        return expenseRepository.findAllForBalanceCalculation(group).stream().map(expenseMapper::toExpenseResponse).collect(Collectors.toList());
    }

    public List<ExpenseResponse> getGroupExpenses(String groupId) {
        Group group = groupService.getActiveById(groupId);

        return expenseRepository.findAllForGroup(group)
                .stream()
                .map(expenseMapper::toExpenseResponse)
                .toList();
    }

    public ExpenseResponse getGroupExpense(String groupId, String expenseId) {
        Group group = groupService.getActiveById(groupId);
        return expenseMapper.toExpenseResponse(expenseRepository.findForGroup(expenseId, group)
                .orElseThrow(() -> new DataNotFoundException("Expense not found")));
    }

    public BigDecimal calculateTotalExpenses(String groupId) {
        return expenseRepository.calculateTotalExpenses(groupId);
    }

    private void validateNoDuplicateParticipants(List<SplitParticipantRequest> requests) {
        Set<String> personIds = new HashSet<>();
        for (SplitParticipantRequest request : requests) {
            if (!personIds.add(request.personId())) {
                throw new DataInvalidException("Duplicate participant");
            }
        }
    }

    private List<SplitParticipant> resolveParticipants(Group group, List<SplitParticipantRequest> requests) {
        return requests.stream()
                .map(request -> {
                    Person person = personService.getById(request.personId());
                    groupMemberService.validateMember(group, person);
                    return new SplitParticipant(person, request.quantity(), request.amount(), request.percentage());
                }).toList();
    }

    private void validateTotal(BigDecimal expenseAmount, List<ExpenseShare> shares) {
        BigDecimal total = shares.stream().map(ExpenseShare::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(expenseAmount) != 0) {
            throw new DataInvalidException("Calculated shares do not equal");
        }
    }

}
