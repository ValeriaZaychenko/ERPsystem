package erp.service.impl;


import erp.domain.Holiday;
import erp.dto.HolidayDto;
import erp.exceptions.DateNotUniqueException;
import erp.exceptions.EntityNotFoundException;
import erp.repository.HolidayRepository;
import erp.service.IDayCounterService;
import erp.utils.DateOrderChecker;
import erp.utils.DtoBuilder;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class DayCounterService implements IDayCounterService{

    @Inject
    private HolidayRepository holidayRepository;

    @Override
    public int countWeekendsBetweenDates(LocalDate begin, LocalDate end) {
        DateOrderChecker.checkEndDateAfterBegin(begin, end);

        int result = 0;
        while (begin.isBefore(end) || begin.isEqual(end)) {
            if (begin.getDayOfWeek() == DayOfWeek.SATURDAY || begin.getDayOfWeek() == DayOfWeek.SUNDAY)
                result++;
            begin = begin.plusDays(1);
        }

        return result;
    }

    @Override
    public int countHolidaysBetweenDates(LocalDate begin, LocalDate end) {
        DateOrderChecker.checkEndDateAfterBegin(begin, end);

        return holidayRepository.findByDateBetweenQueryOrderByDateDesc(begin, end).size();
    }

    @Override
    public int getAllDaysQuantityBetweenDates(LocalDate begin, LocalDate end) {
        DateOrderChecker.checkEndDateAfterBegin(begin, end);

        return (int) ChronoUnit.DAYS.between(begin, end) + 1;
    }

    @Override
    public int getWorkingDaysQuantityBetweenDates(LocalDate begin, LocalDate end) {
        return getAllDaysQuantityBetweenDates(begin, end)
                - countWeekendsBetweenDates(begin, end)
                - countHolidaysBetweenDates(begin, end);
    }

    @Transactional
    @Override
    public String createHoliday(LocalDate date, String description) {
        checkDateIsUnique(date);
        Holiday holiday = new Holiday(date, description);

        holidayRepository.save(holiday);
        return holiday.getId();
    }

    @Transactional
    @Override
    public void editHoliday(String id, LocalDate date, String description) {
        Holiday holiday = restoreHolidayFromRepository(id);

        boolean dateModified = false;
        boolean descriptionModified = false;

        if(!holiday.getDate().equals(date))
            dateModified = true;

        if(!holiday.getDescription().equals(description))
            descriptionModified = true;

        if (!(dateModified || descriptionModified))
            return; //No modification detected

        if(dateModified)
            checkDateIsUnique(date);
            holiday.setDate(date);

        if(descriptionModified)
            holiday.setDescription(description);
    }

    @Transactional
    @Override
    public void deleteHoliday(String id) {
        try {
            holidayRepository.delete(id);
        } catch (DataAccessException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public String copyHolidayToNextYear(String id) {
        Holiday holiday = restoreHolidayFromRepository(id);
        LocalDate oldDate = holiday.getDate();

        LocalDate newDate = getDateForClone(oldDate);
        checkDateIsUnique(newDate);

        Holiday newHoliday = new Holiday(
                newDate,
                holiday.getDescription());

        holidayRepository.save(newHoliday);
        return newHoliday.getId();
    }

    @Override
    public List<HolidayDto> findHolidaysOfYear(int year) {
        List<HolidayDto> dtos = new ArrayList<>();
        LocalDate begin = LocalDate.of(year - 1, 12, 31);
        LocalDate end = LocalDate.of(year + 1, 1, 1);

        List<Holiday> holidays = holidayRepository.findByDateBetweenQueryOrderByDateDesc(begin, end);

        for(Holiday holiday : holidays) {
            dtos.add(DtoBuilder.holidayToDto(holiday));
        }

        return dtos;
    }

    @Transactional
    @Override
    public void copyYearHolidaysToNext(int year) {
        LocalDate begin = LocalDate.of(year - 1, 12, 31);
        LocalDate end = LocalDate.of(year + 1, 1, 1);

        List<Holiday> holidays = holidayRepository.findByDateBetweenQueryOrderByDateDesc(begin, end);

        for(Holiday holiday : holidays) {
            LocalDate oldDate = holiday.getDate();
            LocalDate newDate = getDateForClone(oldDate);
            if (isDateUnique(newDate)) {
                Holiday newHoliday = new Holiday(newDate, holiday.getDescription());
                holidayRepository.save(newHoliday);
            }
        }
    }

    private Holiday restoreHolidayFromRepository(String id) {
        Holiday holiday = holidayRepository.findOne(id);
        if (holiday == null)
            throw new EntityNotFoundException(Holiday.class.getName());

        return holiday;
    }

    /*
    Return 01.03.XXXX if date is 29.02.XXXX or return same date of next year
     */
    private LocalDate getDateForClone(LocalDate date) {
        if (date.getDayOfMonth() == 29 && date.getMonth().getValue() == 2)
            return LocalDate.of(date.getYear() + 1, 3, 1);

        return LocalDate.of(date.getYear() + 1, date.getMonth(), date.getDayOfMonth());
    }

    private boolean checkDateIsUnique(LocalDate date) {
        if (!isDateUnique(date))
            throw new DateNotUniqueException(date);
        return true;
    }

    private boolean isDateUnique(LocalDate date) {
        List<Holiday> holidays = holidayRepository.findByDate(date);
        return holidays.size() == 0;
    }
}
