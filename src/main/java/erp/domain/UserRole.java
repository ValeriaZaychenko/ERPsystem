package erp.domain;

import javax.persistence.Embeddable;

@Embeddable
public enum  UserRole {

    USER,
    LEADER,
    ADMIN
}
