package com.allobank.allobackendtest.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateUtil {

    private DateUtil() {
    }

    public static String dateToString(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            return sdf.format(date);
        } catch (Exception ex) {
            log.error("Error convert date to string", ex);
        }
        return null;
    }

    public static Date stringToDate(String date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            sdf.setLenient(false);
            return sdf.parse(date);
        } catch (ParseException ex) {
            log.error("Error convert string to date", ex);
        }
        return null;
    }

    public static boolean verifyDateFormatFromString(String input, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        if (input != null) {
            try {
                Date convertedDate = sdf.parse(input);
                if (sdf.format(convertedDate).equals(input)) {
                    return true;
                }
            } catch (ParseException e) {
                log.error("Change date format error", e);

            }
        }
        return false;
    }
}