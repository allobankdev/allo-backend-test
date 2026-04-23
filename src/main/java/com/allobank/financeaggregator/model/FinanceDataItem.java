package com.allobank.financeaggregator.model;

public record FinanceDataItem<T>(String resourceType, T data) {
}
