package erp.service.impl;

import erp.domain.MissedDay;
import erp.domain.MissedDayType;
import erp.domain.User;
import erp.dto.MissedDayDto;
import erp.exceptions.EntityNotFoundException;
import erp.exceptions.HolidayOccupiedDateException;
import erp.exceptions.TwoMissedDaysInOneDateException;
import erp.exceptions.UnknownMissedDayTypeException;
import erp.repository.HolidayRepository;
import erp.repository.MissedDayRepository;
import erp.repository.UserRepository;
import erp.service.IMissedDayService;
import erp.utils.DateOrderChecker;
import erp.utils.DtoBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MissedDayService implements IMissedDayService {

    @Inject
    private MissedDayRepository missedDayRepository;
    @Inject
    private HolidayRepository holidayRepository;
    @Inject
    private UserRepository userRepository;

    @Transactional
    @Override
    public String createMissedDay(LocalDate date, String missedDayTypeStr, String userId) {
        MissedDayType missedDayType = parseMissedDateTypeString(missedDayTypeStr);
        User user = restoreUserFromRepository(userId);

        checkCorrectDayForMissedDay(date, user);

        MissedDay missedDay = new MissedDay(date, missedDayType, user);
        missedDayRepository.save(missedDay);
        return missedDay.getId();
    }

    @Transactional
    @Override
    public void editMissedDay(String id, LocalDate date, String missedDayTypeStr, String userId) {
        MissedDayType missedDayType = parseMissedDateTypeString(missedDayTypeStr);
        User user = restoreUserFromRepository(userId);

        MissedDay day = restoreMissedDayFromRepository(id);

        boolean dateModified = false;
        boolean typeModified = false;

        if(!day.getDate().equals(date))
            dateModified = true;

        if(!day.getMissedDayType().equals(missedDayType))
            typeModified = true;

        if (!(dateModified || typeModified))
            return; //No modification detected

        if(dateModified) {
            checkCorrectDayForMissedDay(date, user);

            day.setDate(date);
        }

        if(typeModified)
            day.setMissedDayType(missedDayType);
    }

    @Transactional
    @Override
    public void deleteMissedDay(String id) {
        MissedDay missedDay = restoreMissedDayFromRepository(id);
        missedDayRepository.delete(missedDay);
    }

    @Transactional
    @Override
    public List<MissedDayDto> viewUserMissedDaysBetweenDates(LocalDate begin, LocalDate end, String userId) {
        DateOrderChecker.checkEndDateAfterBegin(begin, end);
        List<MissedDayDto> dtos = new ArrayList<>();
        User user = restoreUserFromRepository(userId);

        List<MissedDay> missedDays = missedDayRepository.findByUserAndBetweenQuery(begin, end, user);
        for(MissedDay day : missedDays) {
            dtos.add(DtoBuilder.missedDayToDto(day));
        }

        return dtos;
    }

    @Transactional
    @Override
    public void markDayOffAsVacation(String id) {
        MissedDay missedDay = restoreMissedDayFromRepository(id);
        missedDay.setMissedDayType(MissedDayType.VACATION);
    }

    private User restoreUserFromRepository(String userId) {
        User user = userRepository.findOne(userId);
        if (user == null)
            throw new EntityNotFoundException(User.class.getName());

        return user;
    }

    private MissedDay restoreMissedDayFromRepository(String id) {
        MissedDay missedDay = missedDayRepository.findOne(id);
        if (missedDay == null)
            throw new EntityNotFoundException(MissedDay.class.getName());

        return missedDay;
    }

    private void checkCorrectDayForMissedDay(LocalDate date, User user) {
        if (holidayRepository.findByDate(date).size() > 0)
            throw new HolidayOccupiedDateException(date.toString());

        if (missedDayRepository.findByUserAndDate(user, date) != null)
            throw new TwoMissedDaysInOneDateException(date.toString());
    }

    private MissedDayType parseMissedDateTypeString(String missedDayTypeStr) {
        MissedDayType missedDayType = null;

        try {
            missedDayType = MissedDayType.valueOf(missedDayTypeStr);
        } catch (IllegalArgumentException e) {
            throw new UnknownMissedDayTypeException(missedDayTypeStr);
        }
        return missedDayType;
    }
}
