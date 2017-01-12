package erp.service.impl;

import erp.domain.User;
import erp.domain.UserRole;
import erp.dto.UserDto;
import erp.event.RemoveUserEvent;
import erp.exceptions.DuplicateEmailException;
import erp.exceptions.EntityNotFoundException;
import erp.exceptions.MismatchPasswordException;
import erp.exceptions.UnknownRoleException;
import erp.repository.UserRepository;
import erp.service.IAuthenticationService;
import erp.service.IMailService;
import erp.service.IPasswordService;
import erp.service.IUserService;
import erp.utils.DtoBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements IUserService, IAuthenticationService {

    @Inject
    private UserRepository userRepository;
    @Inject
    private IPasswordService passwordService;
    @Inject
    private IMailService mailService;
    @Inject
    private ApplicationEventPublisher eventPublisher;

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
    May be discussed
     */
    @Transactional
    @Override
    public void removeUser(String id) {
        User user = restoreUserFromRepository(id);
        eventPublisher.publishEvent(new RemoveUserEvent(this, id));
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public UserDto findUserById(String id) {
        User user = restoreUserFromRepository(id);
        return DtoBuilder.userToDto(user);
    }

    @Transactional
    @Override
    public void changePassword(String id, String oldPassword, String newPassword) {
        User user = restoreUserFromRepository(id);

        if(passwordService.comparePasswords(oldPassword, user.getHashedPassword())) {
            user.setHashedPassword(passwordService.getHashFromPassword(newPassword));
            if (user.isNeedToChangePassword())
                user.setNeedToChangePassword(false);
        }
        else
            throw new MismatchPasswordException();
    }

    @Transactional
    @Override
    public List<UserDto> viewUsers() {
        List<User> users = userRepository.findAllByOrderByNameAsc();
        List<UserDto> userDtos = new ArrayList<>();
        for(User user : users) {
            userDtos.add(DtoBuilder.userToDto(user));
        }
        return userDtos;
    }

    @Transactional
    @Override
    public UserDto authenticate(Authentication authentication) {
        UsernamePasswordAuthenticationToken credentials = (UsernamePasswordAuthenticationToken) authentication;
        String email = credentials.getPrincipal().toString();
        String password = credentials.getCredentials().toString();
        credentials.eraseCredentials();

        User user = userRepository.findFirstByEmail(email);

        if (user == null) {
            return null;
        }

        if (!passwordService.comparePasswords(password, user.getHashedPassword())) {
            return null;
        }

        UserDto dto = DtoBuilder.userToDto(user);
        dto.setAuthenticated(true);

        switch (user.getUserRole())
        {
            case USER:
            case LEADER:
                dto.addAuthority("AUTH_USER");
                break;

            case ADMIN:
                dto.addAuthority("AUTH_USER");
                dto.addAuthority("AUTH_ADMIN");
                break;

            default:
                throw new RuntimeException();
        }

        return dto;
    }

    @Override
    public boolean needToChangePassword(UserDto dto) {
        return userRepository.findOne(dto.getId()).isNeedToChangePassword();
    }

    @Override
    public boolean supports(Class< ? > aClass)
    {
        return aClass == UsernamePasswordAuthenticationToken.class;
    }

    private User restoreUserFromRepository(String id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new EntityNotFoundException(User.class.getName());
        }
        return user;
    }

    private void checkEmailIsUnique(String email) {
        if(!isEmailUnique(email)) {
            throw new DuplicateEmailException(email);
        }
    }

    private boolean isEmailUnique(String email) {
        return (userRepository.findByEmail(email) == null);
    }

    private UserRole restoreUserRoleFromString(String userRole) {
        UserRole role = null;

        try {
            role = UserRole.valueOf(userRole);
        }
        catch (IllegalArgumentException e) {
            throw new UnknownRoleException(userRole);
        }
        return role;
    }
}
