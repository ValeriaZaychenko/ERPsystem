package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ErrorKeys;
import erp.controller.constants.ViewNames;
import erp.exceptions.DomainLogicException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ExceptionHandlingAdvice {

    //CUSTOM EXCEPTION HANDLERS----------------------------------------------------------------------------------------

    /*
    Include MismatchPasswordException, UnknownRoleException, DuplicateEmailException,
    EntityNotFoundException, InvalidDateException, DateNotUniqueException, WorkloadIncompatibilityException
     */
    @ExceptionHandler(DomainLogicException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle(DomainLogicException e) {
        return getModelAndView(e.getName(), e);
    }

    //STANDART EXCEPTION HANDLERS--------------------------------------------------------------------------------------

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle(NoHandlerFoundException e)  {
        return getModelAndView(ErrorKeys.NoHandlerMessage, e);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle(DateTimeParseException e)  {
        return getModelAndView(ErrorKeys.DateParseMessage, e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle(IllegalArgumentException e)  {
        return getModelAndView(ErrorKeys.IllegalArgumentMessage, e);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle (ConstraintViolationException e) {
        return getModelAndView(ErrorKeys.ConstraintViolationMessage, e);
    }

    //-----------------------------------------------------------------------------------------------------------------

    private ModelAndView getModelAndView(String message, Exception e) {
        ModelAndView mav = new ModelAndView(ViewNames.ERROR.error);
        mav.addObject(AttributeNames.ErrorView.message, message);
        mav.addObject(AttributeNames.ErrorView.attribute, e.getMessage());
        return mav;
    }
}
