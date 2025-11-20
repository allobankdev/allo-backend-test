package com.athallah.finance.config.message;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizedMessages {
    /**
    * Retrieves the value for the messageKey from the locale-specific messages_en.properties, or from
    * the base messages_en.properties for unsupported locales.
    */
    private static final String RESOURCE_BUNDLE_BASE_NAME = "messages";
    private static final Locale ENGLISH_LOCALE = Locale.ENGLISH;
    private static final Locale INDONESIAN_LOCALE = new Locale("IND");

    private LocalizedMessages() {}

    /*
     * For more complex implementations, you will want a var-args parameter for MessageFormat
     * substitutions. Then we can read the value from the bundle and pass the value with the
     * substitutions to MessageFormat to create the final message value.
     */
    public static String getLocalizedMessageIndonesian(String messageKey) {
        return ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, INDONESIAN_LOCALE)
                .getString(messageKey);
    }

    public static String getLocalizedMessageEnglish(String messageKey) {
        return ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, ENGLISH_LOCALE)
                .getString(messageKey);
    }

    public static String getLocalizedMessageIndonesian(String messageKey, String... args) {
        String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, INDONESIAN_LOCALE).getString(messageKey);
        return String.format(message, args);
    }

    public static String getLocalizedMessageEnglish(String messageKey, String... args) {
        String message = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, ENGLISH_LOCALE).getString(messageKey);
        return String.format(message, args);
    }
}
