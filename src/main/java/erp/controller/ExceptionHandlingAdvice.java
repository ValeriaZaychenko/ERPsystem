package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ErrorKeys;
import erp.controller.constants.ViewNames;
import erp.exceptions.*;
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

    @ExceptionHandler(InvalidDateException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle (InvalidDateException e) {
        ModelAndView mav = new ModelAndView(ViewNames.ERROR.error);
        mav.addObject(AttributeNames.ErrorView.message, ErrorKeys.InvalidDateMessage);
        mav.addObject(AttributeNames.ErrorView.attribute, e.getMessage());
        return mav;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle (EntityNotFoundException e) {
        ModelAndView mav = new ModelAndView(ViewNames.ERROR.error);
        mav.addObject(AttributeNames.ErrorView.message, ErrorKeys.EntityNotFoundMessage);
        mav.addObject(AttributeNames.ErrorView.attribute, e.getMessage());
        return mav;
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle (DuplicateEmailException e) {
        ModelAndView mav = new ModelAndView(ViewNames.ERROR.error);
        mav.addObject(AttributeNames.ErrorView.message, ErrorKeys.DuplicateEmailMessage);
        mav.addObject(AttributeNames.ErrorView.attribute, e.getMessage());
        return mav;
    }

    @ExceptionHandler(UnknownRoleException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle (UnknownRoleException e) {
        ModelAndView mav = new ModelAndView(ViewNames.ERROR.error);
        mav.addObject(AttributeNames.ErrorView.message, ErrorKeys.UnknownRoleMessage);
        mav.addObject(AttributeNames.ErrorView.attribute, e.getMessage());
        return mav;
    }

    @ExceptionHandler(MismatchPasswordException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle (MismatchPasswordException e) {
        ModelAndView mav = new ModelAndView(ViewNames.ERROR.error);
        mav.addObject(AttributeNames.ErrorView.message, ErrorKeys.MismatchPasswordMessage);
        mav.addObject(AttributeNames.ErrorView.attribute, e.getMessage());
        return mav;
    }

    //STANDART EXCEPTION HANDLERS--------------------------------------------------------------------------------------

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle(NoHandlerFoundException e)  {
        ModelAndView mav = new ModelAndView(ViewNames.ERROR.error);
        mav.addObject(AttributeNames.ErrorView.message, ErrorKeys.NoHandlerMessage);
        mav.addObject(AttributeNames.ErrorView.attribute, e.getMessage());
        return mav;
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle(DateTimeParseException e)  {
        ModelAndView mav = new ModelAndView(ViewNames.ERROR.error);
        mav.addObject(AttributeNames.ErrorView.message, ErrorKeys.DateParseMessage);
        mav.addObject(AttributeNames.ErrorView.attribute, e.getMessage());
        return mav;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle(IllegalArgumentException e)  {
        ModelAndView mav = new ModelAndView(ViewNames.ERROR.error);
        mav.addObject(AttributeNames.ErrorView.message, ErrorKeys.IllegalArgumentMessage);
        mav.addObject(AttributeNames.ErrorView.attribute, e.getMessage());
        return mav;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handle (ConstraintViolationException e) {
        ModelAndView mav = new ModelAndView(ViewNames.ERROR.error);
        mav.addObject(AttributeNames.ErrorView.message, ErrorKeys.ConstraintViolationMessage);
        mav.addObject(AttributeNames.ErrorView.attribute, e.getMessage());
        return mav;
    }
}
