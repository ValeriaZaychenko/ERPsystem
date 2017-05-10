package erp.dto;

import java.io.Serializable;

public class CalendarDto implements Serializable {

    private int weekends;
    private int holidays;
    private int workdays;
    private int allDays;

    public int getWeekends() {
        return weekends;
    }

    public void setWeekends(int weekends) {
        this.weekends = weekends;
    }

    public int getHolidays() {
        return holidays;
    }

    public void setHolidays(int holidays) {
        this.holidays = holidays;
    }

    public int getWorkdays() {
        return workdays;
    }

    public void setWorkdays(int workdays) {
        this.workdays = workdays;
    }

    public int getAllDays() {
        return allDays;
    }

    public void setAllDays(int allDays) {
        this.allDays = allDays;
    }
}
