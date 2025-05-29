package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static String formatDateTime(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
}