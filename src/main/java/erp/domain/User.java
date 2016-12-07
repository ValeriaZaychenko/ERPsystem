package erp.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity(name = "employees")
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true)
    private String id;
    private String name;
    private String email;
    private byte[] hashedPassword;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String name, String email, UserRole userRole, byte[] hashedPassword) {
        this.name = name;
        this.email = email;
        this.userRole = userRole;
        this.hashedPassword = hashedPassword;
    }

    protected User(){}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(byte[] hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
