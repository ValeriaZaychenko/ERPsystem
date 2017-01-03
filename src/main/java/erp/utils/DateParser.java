package erp.utils;


import erp.exceptions.InvalidDateException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateParser {

    public static final DateTimeFormatter wholeDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

    public static LocalDate parseDate(String date) {
        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(date, wholeDateFormatter);
            return localDate;
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(date);
        }
    }

    public static LocalDate parseMonthDate(String date) {
        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(date + "-01", wholeDateFormatter );
            return localDate;
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(date);
        }
    }
}
