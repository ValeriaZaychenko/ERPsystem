package erp.exceptions;


import erp.controller.constants.ErrorKeys;

public class UnknownGroupByException extends DomainLogicException {

    public UnknownGroupByException(String groupBy) {
            super(groupBy);
            setName(ErrorKeys.UnknownGroupByMessage);
        }
}
