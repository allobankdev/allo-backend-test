package com.allobank.splitbill.domain;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * A split instruction understood by the domain layer.
 *
 * <p>The populated value depends on the split strategy: {@code amount} is used
 * by {@link SplitType#EXACT}, {@code percentage} by
 * {@link SplitType#PERCENTAGE}, and neither by {@link SplitType#EQUAL}.</p>
 */
public record SplitShare(UUID participantId, BigDecimal amount, BigDecimal percentage) {
}
