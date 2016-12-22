package erp.exceptions;


public class InvalidDateException extends RuntimeException {

    public InvalidDateException(String date) {
        super(date);
    }
}
