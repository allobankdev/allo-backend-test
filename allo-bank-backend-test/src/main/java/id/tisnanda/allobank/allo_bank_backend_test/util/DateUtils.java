package id.tisnanda.allobank.allo_bank_backend_test.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(outputFormatter);
    }
}
