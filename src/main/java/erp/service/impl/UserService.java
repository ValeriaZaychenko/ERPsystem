package erp.service.impl;

import erp.domain.User;
import erp.domain.UserRole;
import erp.dto.UserDto;
import erp.repository.UserRepository;
import erp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements IUserService {

    @Inject
    private UserRepository userRepository;

    @Transactional
    @Override
    public String createUser(String name, String email, String userRole) {
        UserRole role = UserRole.valueOf(userRole);
        User user = new User(name, email, role);
        userRepository.save(user);
        return user.getId();
    }

    @Transactional
    @Override
    public void changeUserName(String id, String name) {
        User user = restoreUserFromRepository(id);
        user.setName(name);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void changeUserEmail(String id, String email) {
        User user = restoreUserFromRepository(id);
        user.setEmail(email);
        userRepository.save(user);
    }

    @Override
    public void changeUserRole(String id, String userRole) {
        User user = restoreUserFromRepository(id);
        user.setUserRole(UserRole.valueOf(userRole));
        userRepository.save(user);
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

    private User restoreUserFromRepository(String id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new RuntimeException("Database doesn't have this user");
        }
        return user;
    }
}
