package erp.utils;


import erp.exceptions.InvalidDateException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateParser {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate parseDate(String date) {
        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(date, formatter);
            return localDate;
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(date);
        }
    }
}
