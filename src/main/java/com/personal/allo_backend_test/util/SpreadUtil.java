package com.personal.allo_backend_test.util;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpreadUtil {
  public static Double buySpread(String username, Double rate) {
    return Optional.ofNullable(username)
      .filter(StringUtils::isNotEmpty)
      .map(String::trim)
      .map(String::toLowerCase)
      .map(lowered -> lowered.chars().sum())
      .map(sumOfUnicodeValues -> (sumOfUnicodeValues % 1_000) / 100_000.0)
      .map(spreadFactor -> Optional.ofNullable(rate)
        .filter(val -> val > 0.0)
        .map(val -> (1 / val) * (1 + spreadFactor))
        .orElse(0.0))
      .orElse(0.0);
  }
}
