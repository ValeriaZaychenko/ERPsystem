package erp.dto;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDto implements Serializable, Authentication{

    private String id;
    private String name;
    private String email;
    private String userRole;
    private String hashedPassword;
    private List<GrantedAuthority> authorities = new ArrayList<>();;
    private boolean authenticated;

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

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }


    @Override
    public Collection< ? extends GrantedAuthority > getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return getHashedPassword();
    }

    @Override
    public Object getDetails() {
        return getName();
    }

    @Override
    public Object getPrincipal() {
        return getEmail();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        authenticated = b;
    }

    public void addAuthority(String authorityName) {
        authorities.add(new SimpleGrantedAuthority(authorityName));
    }
}
