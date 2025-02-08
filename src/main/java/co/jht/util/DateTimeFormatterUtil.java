package co.jht.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static co.jht.constants.ApplicationConstants.ASIA_TOKYO;
import static co.jht.constants.ApplicationConstants.ASIA_TOKYO_FORMAT;

public class DateTimeFormatterUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(ASIA_TOKYO_FORMAT);

    public static DateTimeFormatter getFormatter() {
        return FORMATTER;
    }

    public static ZonedDateTime getCurrentTokyoTime() {
        return ZonedDateTime.parse(ZonedDateTime.now(ZoneId.of(ASIA_TOKYO)).format(FORMATTER));
    }
}