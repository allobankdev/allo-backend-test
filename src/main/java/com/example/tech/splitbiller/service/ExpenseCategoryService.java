package com.example.tech.splitbiller.service;

import com.example.tech.splitbiller.entity.ExpenseCategory;
import com.example.tech.splitbiller.entity.IdempotencyKey;
import com.example.tech.splitbiller.exception.DataNotFoundException;
import com.example.tech.splitbiller.exception.TransactionConflictException;
import com.example.tech.splitbiller.mapper.ExpenseCategoryMapper;
import com.example.tech.splitbiller.model.request.CreateExpenseCategoryRequest;
import com.example.tech.splitbiller.model.request.UpdateExpenseCategoryRequest;
import com.example.tech.splitbiller.model.response.ExpenseCategoryResponse;
import com.example.tech.splitbiller.repository.ExpenseCategoryRepository;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final IdempotencyKeyService idempotencyKeyService;
    private final ObjectMapper objectMapper;
    private final ExpenseCategoryMapper expenseCategoryMapper;


    public ExpenseCategoryService(ExpenseCategoryRepository expenseCategoryRepository,
                                  IdempotencyKeyService idempotencyKeyService,
                                  ObjectMapper objectMapper,
                                  ExpenseCategoryMapper expenseCategoryMapper) {
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.idempotencyKeyService = idempotencyKeyService;
        this.objectMapper = objectMapper;
        this.expenseCategoryMapper = expenseCategoryMapper;
    }

    public List<ExpenseCategoryResponse> getAllExpenseCategories() {
        return expenseCategoryRepository.findByDeleted(Boolean.FALSE).stream().map(expenseCategoryMapper::toExpenseCategoryResponse).toList();
    }

    public ExpenseCategoryResponse getExpenseCategory(String id) {
        return expenseCategoryMapper.toExpenseCategoryResponse(
                expenseCategoryRepository.findByIdAndDeleted(id, Boolean.FALSE)
                        .orElseThrow(() -> new DataNotFoundException("Expense category not found")));
    }

    public ExpenseCategoryResponse createExpenseCategory(String idempotencyKey, CreateExpenseCategoryRequest request) {
        String hashedRequest = objectMapper.writeValueAsString(request);
        Optional<IdempotencyKey> find = idempotencyKeyService.findByIdempotencyKey(idempotencyKey);
        if(find.isPresent()) {
            IdempotencyKey idempotencyKeyObj = find.get();
            if(!idempotencyKeyObj.getHashRequest().equals(hashedRequest))
                throw new TransactionConflictException("Idempotency-Key was already used with a different request");

            String response = idempotencyKeyObj.getResponse();
            return objectMapper.readValue(response, ExpenseCategoryResponse.class);
        }

        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setName(request.name());
        expenseCategory.setCreatedAt(Instant.now().toEpochMilli());
        expenseCategory.setDeleted(Boolean.FALSE);
        ExpenseCategory saved = expenseCategoryRepository.save(expenseCategory);
        ExpenseCategoryResponse response = expenseCategoryMapper.toExpenseCategoryResponse(saved);
        idempotencyKeyService.save(idempotencyKey, hashedRequest, objectMapper.writeValueAsString(response));
        return response;
    }

    public ExpenseCategoryResponse updateExpenseCategory(String idempotencyKey, UpdateExpenseCategoryRequest request) {
        String hashedRequest = objectMapper.writeValueAsString(request);
        Optional<IdempotencyKey> find = idempotencyKeyService.findByIdempotencyKey(idempotencyKey);
        if(find.isPresent()) {
            IdempotencyKey idempotencyKeyObj = find.get();
            if(!idempotencyKeyObj.getHashRequest().equals(hashedRequest))
                throw new TransactionConflictException("Idempotency-Key was already used with a different request");

            String response = idempotencyKeyObj.getResponse();
            return objectMapper.readValue(response, ExpenseCategoryResponse.class);
        }

        ExpenseCategory expenseCategory = getByIdAndDeleted(request.id());
        expenseCategory.setName(request.name());
        ExpenseCategory saved = expenseCategoryRepository.save(expenseCategory);
        ExpenseCategoryResponse response = expenseCategoryMapper.toExpenseCategoryResponse(saved);
        idempotencyKeyService.save(idempotencyKey, hashedRequest, objectMapper.writeValueAsString(response));
        return response;
    }

    public void deleteExpense(String id) {
        ExpenseCategory expenseCategory = getByIdAndDeleted(id);
        expenseCategory.setDeleted(Boolean.TRUE);
        expenseCategoryRepository.save(expenseCategory);
    }

    public ExpenseCategory getByIdAndDeleted(String id) {
        return expenseCategoryRepository.findByIdAndDeleted(id, Boolean.FALSE)
                .orElseThrow(() -> new DataNotFoundException("Expense category not found"));
    }
}
