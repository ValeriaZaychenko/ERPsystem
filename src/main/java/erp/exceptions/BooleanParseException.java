package erp.exceptions;


public class BooleanParseException extends RuntimeException {

    public BooleanParseException() {
        super("Can't parse from string to boolean");
    }
}
