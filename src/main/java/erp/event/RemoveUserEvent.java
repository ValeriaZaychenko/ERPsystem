package erp.event;


import org.springframework.context.ApplicationEvent;

public class RemoveUserEvent extends ApplicationEvent{

    private String userId;

    public RemoveUserEvent(Object c, String userId) {
        super(c);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
