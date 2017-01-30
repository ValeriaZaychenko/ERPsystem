package erp.exceptions;


import erp.controller.constants.ErrorKeys;

public class HolidayOccupiedDateException extends DomainLogicException {

    public HolidayOccupiedDateException(String date) {
        super(date);
        setName(ErrorKeys.HolidayOccupiedDateMessage);
    }
}
