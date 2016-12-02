package erp.dto;


public class UserDto {

    private String id;
    private String name;
    private String email;
    private String userRole;

    public UserDto(String id, String name, String email, String userRole) {
        this.name = name;
        this.email = email;
        this.userRole = userRole;
    }

    public String getId() {
        return id;
    }

    public void setId() {
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

    private void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
