package erp.service.impl;

import erp.domain.User;
import erp.domain.UserRole;
import erp.dto.UserDto;
import erp.repository.UserRepository;
import erp.service.IMailService;
import erp.service.IPasswordService;
import erp.service.IUserService;
import erp.utils.DtoBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements IUserService {

    @Inject
    private UserRepository userRepository;
    @Inject
    private IPasswordService passwordService;
    @Inject
    private IMailService mailService;

    @Transactional
    @Override
    public String createUser(String name, String email, String userRole) {
        UserRole role = restoreUserRoleFromString(userRole);
        checkEmailIsUnique(email);

        String randomPassword = passwordService.generatePassword();

        mailService.writeDataToFile(randomPassword);

        User user = new User(
                                 name,
                                 email,
                                 role,
                                 passwordService.getHashFromPassword(randomPassword));

        userRepository.save(user);
        return user.getId();
    }

    @Transactional
    @Override
    public void editUser(String id, String name, String email, String userRole) {
        User user = restoreUserFromRepository(id);

        boolean nameModified = false;
        boolean emailModified = false;
        boolean roleModified = false;

        UserRole role = null;

        if(!user.getName().equals(name))
            nameModified = true;

        if(!user.getEmail().equals(email))
            emailModified = true;

        if(!user.getUserRole().toString().equals(userRole))
            roleModified = true;

        if (!(nameModified || roleModified || emailModified))
            return; //No modification detected


        if(emailModified)
            checkEmailIsUnique(email);

        if(roleModified)
            role = restoreUserRoleFromString(userRole);


        if(nameModified)
            user.setName(name);

        if(emailModified)
            user.setEmail(email);

        if(roleModified)
            user.setUserRole(role);
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
    public UserDto findUser(String id) {
        User user = restoreUserFromRepository(id);
        return DtoBuilder.toDto(user);
    }

    @Transactional
    @Override
    public void changePassword(String id, String oldPassword, String newPassword) {
        User user = restoreUserFromRepository(id);

        if(passwordService.comparePasswords(oldPassword, user.getHashedPassword())) {
            user.setHashedPassword(passwordService.getHashFromPassword(newPassword));
        }
        else
            throw new RuntimeException("Password doesn't match with old password");
    }

    @Transactional
    @Override
    public List<UserDto> viewUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for(User user : users) {
            userDtos.add(DtoBuilder.toDto(user));
        }
        return userDtos;
    }

    @Transactional
    @Override
    public UserDto authenticate(String userLogin, String password) {
        User user = userRepository.findFirstByEmail(userLogin);

        if(user == null) {
            throw new RuntimeException("Db doesn't have user with this login");
        }

        if(!passwordService.comparePasswords(password, user.getHashedPassword())) {
            throw new RuntimeException("Password doesn't match");
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
        if(!isEmailUnique(email)) {
            throw new RuntimeException("The database already has the user with this email");
        }
    }

    private boolean isEmailUnique(String email) {
        return (userRepository.findByEmail(email).isEmpty());
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
}
