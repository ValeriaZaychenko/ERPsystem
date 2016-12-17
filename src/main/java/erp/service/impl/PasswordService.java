package erp.service.impl;


import erp.service.IPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
public class PasswordService implements IPasswordService {

    @Inject
    private PasswordEncoder passwordEncoder;

    @Override
    public byte[]  getHashFromPassword(String password) {
        String encodedPassword = passwordEncoder.encode(password);
        return encodedPassword.getBytes();
    }

    @Override
    public boolean comparePasswords(String password, byte[] hashedPassword) {
        return passwordEncoder.matches(
                password, new String(hashedPassword, StandardCharsets.UTF_8));
    }

    @Override
    public String generatePassword() {
        Random rand = new Random();
        Integer password = rand.nextInt(40);
        return password.toString();
    }
}
