package erp.dto;


import java.io.Serializable;

public class UserDto implements Serializable{

    private String id;
    private String name;
    private String email;
    private String userRole;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
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

    public String  getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
