package erp.service.impl;

import erp.domain.User;
import erp.domain.UserRole;
import erp.dto.UserDto;
import erp.repository.UserRepository;
import erp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements IUserService {

    @Inject
    private UserRepository userRepository;

    @Transactional
    @Override
    public String createUser(String name, String email, String userRole) {
        UserRole role = restoreUserRoleFromString(userRole);
        checkEmailIsUnique(email);

        User user = new User(name, email, role);
        userRepository.save(user);
        return user.getId();
    }

    @Transactional
    @Override
    public void editUser(String id, String name, String email, String userRole) {
        User user = restoreUserFromRepository(id);

        if(!user.getName().equals(name)) {
            changeUserName(user, name);
        }

        if(!user.getEmail().equals(email)) {
            changeUserEmail(user, email);
        }

        if(!user.getUserRole().equals(userRole)) {
            changeUserRole(user, userRole);
        }
    }

    /*
    CRUD Repository has own delete methods by id and by entity.
    I've choosed deleting by entity because of adding another ids at frontside in the future.
    TODO May be discussed
     */
    @Transactional
    @Override
    public void removeUser(String id) {
        User user = restoreUserFromRepository(id);
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public User findUser(String id) {
        User user = restoreUserFromRepository(id);
        return user;
    }

    @Transactional
    @Override
    public Iterable<UserDto> viewUsers() {
        Iterable<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for(User user : users) {
            userDtos.add(DtoBuilder.toDto(user));
        }
        return userDtos;
    }

    @Transactional
    @Override
    public UserDto authenticate(String userLogin) {
        User user = userRepository.findFirstByEmail(userLogin);

        if(user == null) {
            throw new RuntimeException("Db doesn't have user with this login");
        }

        return DtoBuilder.toDto(user);
    }

    private User restoreUserFromRepository(String id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new RuntimeException("Database doesn't have this user");
        }
        return user;
    }

    private void checkEmailIsUnique(String email) {
        if(!userRepository.findByEmail(email).isEmpty()) {
            throw new RuntimeException("The database already has the user with this email");
        }
    }

    private UserRole restoreUserRoleFromString(String userRole) {
        UserRole role = null;

        try {
            role = UserRole.valueOf(userRole);
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Couldn't parse value from string to enum value at user role");
        }
        return role;
    }

    private void changeUserName( User user, String name) {
        user.setName(name);
        userRepository.save(user);
    }

    private void changeUserEmail( User user, String email) {
        checkEmailIsUnique(email);

        user.setEmail(email);
        userRepository.save(user);
    }

    private void changeUserRole( User user, String userRole) {
        UserRole role = restoreUserRoleFromString(userRole);

        user.setUserRole(role);
        userRepository.save(user);
    }
}
