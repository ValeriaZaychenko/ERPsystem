package erp.exceptions;


import erp.controller.constants.ErrorKeys;

public class EntityNotFoundException extends DomainLogicException {

    public EntityNotFoundException(String className) {
        super(className);
        setName(ErrorKeys.EntityNotFoundMessage);
    }
}
