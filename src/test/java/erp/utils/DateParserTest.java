package erp.utils;


import erp.exceptions.InvalidDateException;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class DateParserTest {

    @Test
    public void parseDateCorrectly() {
        LocalDate date = DateParser.parseDate("2016-11-11");

        assertEquals(date.toString(), "2016-11-11");
    }

    @Test(expected = InvalidDateException.class)
    public void parseInvalidString() {
        DateParser.parseDate("blabla");
    }

    @Test(expected = InvalidDateException.class)
    public void parseInvalidDate() {
        DateParser.parseDate("2016-30-40");
    }
}
