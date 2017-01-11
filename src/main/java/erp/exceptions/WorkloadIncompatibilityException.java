package erp.exceptions;


import erp.controller.constants.ErrorKeys;

public class WorkloadIncompatibilityException extends DomainLogicException {

    public WorkloadIncompatibilityException(String duration) {
        super(duration);
        setName(ErrorKeys.WorkloadIncompatibilityMessage);
    }
}
