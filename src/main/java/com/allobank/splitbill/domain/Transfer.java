package com.allobank.splitbill.domain;

import java.math.BigDecimal;
import java.util.UUID;

/** A domain settlement instruction from one group participant to another. */
public record Transfer(UUID from, UUID to, BigDecimal amount) {
}
