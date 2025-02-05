package co.jht.util;

import java.time.format.DateTimeFormatter;

public class DateTimeFormatterUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    public static DateTimeFormatter getFormatter() {
        return FORMATTER;
    }
}