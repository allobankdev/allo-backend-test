package com.allobank.finance.model;

// Todo : finance data result untuk menyatukan semua tipe resource dalam satu format yang seragam
public record FinanceDataResult(String resourceType, Object data) {
}
