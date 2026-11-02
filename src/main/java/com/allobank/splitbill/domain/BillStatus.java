package com.allobank.splitbill.domain;

/** A user's current position in a group after expenses and recorded payments. */
public enum BillStatus {
    YOU_OWE,
    YOU_ARE_OWED,
    SETTLED
}
