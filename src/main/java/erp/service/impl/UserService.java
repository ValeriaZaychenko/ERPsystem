package erp.service.impl;

import erp.domain.User;
import erp.domain.UserRole;
import erp.repository.UserRepository;
import erp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.jws.soap.SOAPBinding;


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
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new RuntimeException("Database doesn't have this user");
        }
        user.setName(name);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void changeUserEmail(String id, String email) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new RuntimeException("Database doesn't have this user");
        }
        user.setEmail(email);
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
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new RuntimeException("Database doesn't have this user");
        }
        userRepository.delete(user);
    }

    @Override
    public User findUser(String id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new RuntimeException("Database doesn't have this user");
        }
        return user;
    }

    @Transactional
    @Override
    public Iterable<User> viewUsers() {
        Iterable<User> users = userRepository.findAll();
        return users;
    }
}
