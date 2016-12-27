package erp.exceptions;


public class DomainLogicException extends RuntimeException {

    private String name;

    public DomainLogicException(String attribute) {
        super(attribute);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttribute() {
        return this.getMessage();
    }
}
