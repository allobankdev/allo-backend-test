package com.allobank.allobackendtest.utility;

import java.sql.Timestamp;

public class CurrentTimestampUtility {
  public static Timestamp getLocalDateTime() {
    return Timestamp.valueOf(java.time.LocalDateTime.now());
  }
}
