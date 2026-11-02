package com.allobank.splitbill.domain;

/** Identifies an original payment or its append-only compensating entry. */
public enum PaymentType {
    PAYMENT,
    REVERSAL
}
