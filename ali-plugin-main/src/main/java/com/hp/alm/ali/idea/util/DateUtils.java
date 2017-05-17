package com.hp.alm.ali.idea.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by joyeuxl on 17.05.2017.
 */
public final class DateUtils {
    public static final DateFormat SHORT_LOCALE_DATE_FORMAT;

    static {
        Locale locale = Locale.getDefault();
        DateFormat dateInstance = DateFormat.getDateInstance(DateFormat.SHORT, locale);

        if (dateInstance instanceof SimpleDateFormat) {
            String pattern = ((SimpleDateFormat) dateInstance).toPattern();
            pattern = pattern.replaceAll("y+", "yyyy");
            dateInstance = new SimpleDateFormat(pattern);
        }
        SHORT_LOCALE_DATE_FORMAT = dateInstance;
    }

    private DateUtils() {
    }
}
