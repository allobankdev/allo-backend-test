package com.athallah.finance.config.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class GlobalResponseMessage {

    private Boolean success;
    private String message;
    private Object data;

    // Inject MessageSource (jika menggunakan i18n)
    private static MessageSource messageSource;

    public static void setMessageSource(MessageSource messageSource) {
        GlobalResponseMessage.messageSource = messageSource;
    }

    /**
     * Build success response dengan message dari message bundle
     */
    protected void buildSuccessResponse(String messageKey, String... messageArgs) {
        this.success = true;
        this.message = resolveMessage(messageKey, messageArgs);
    }

    /**
     * Build error response dengan message dari message bundle
     */
    protected void buildErrorResponse(String messageKey, String... messageArgs) {
        this.success = false;
        this.message = resolveMessage(messageKey, messageArgs);
    }

    /**
     * Resolve message dari MessageSource (i18n support)
     * Jika MessageSource tidak diset, gunakan messageKey sebagai fallback
     */
    private String resolveMessage(String messageKey, String... messageArgs) {
        if (messageSource != null) {
            try {
                Locale locale = LocaleContextHolder.getLocale();
                return messageSource.getMessage(messageKey, messageArgs, locale);
            } catch (Exception e) {
                // Fallback ke messageKey jika tidak ditemukan
                return messageKey;
            }
        }
        return messageKey;
    }
}