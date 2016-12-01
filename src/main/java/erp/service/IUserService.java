package erp.service;


import erp.domain.User;

import java.util.List;

public interface IUserService {

    String createUser(String name, String email, String userRole);
    void changeUserName(String id, String name);
    void changeUserEmail(String id, String email);
    void removeUser(String id);
    User findUser(String id);

    Iterable<User> viewUsers();
}
