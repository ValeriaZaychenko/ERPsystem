package erp.controller;

import erp.exceptions.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ExceptionHandlingAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handle (ConstraintViolationException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }

    @ExceptionHandler(InvalidDateException.class)
    public ModelAndView handle (InvalidDateException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handle (EntityNotFoundException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ModelAndView handle (DuplicateEmailException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }

    @ExceptionHandler(UnknownRoleException.class)
    public ModelAndView handle (UnknownRoleException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }

    @ExceptionHandler(MismatchPasswordException.class)
    public ModelAndView handle (MismatchPasswordException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handle(NoHandlerFoundException e)  {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ModelAndView handle(DateTimeParseException e)  {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handle(IllegalArgumentException e)  {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }
}
