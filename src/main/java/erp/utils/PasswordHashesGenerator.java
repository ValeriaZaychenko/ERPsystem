package erp.utils;


import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public final class PasswordHashesGenerator {

    private static final SecureRandom RANDOM;
    private static final int HASHING_ROUNDS = 10;
    static {
        try {
            RANDOM = SecureRandom.getInstanceStrong();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getHashFromPassword(String password){
        String salt = BCrypt.gensalt(HASHING_ROUNDS, RANDOM);
        return BCrypt.hashpw(password, salt).getBytes();
    }

    public static boolean comparePasswords(String password, byte[] hashedPassword) {
        return BCrypt.checkpw(password,
                new String(hashedPassword, StandardCharsets.UTF_8));
    }

    public static String generatePassword() {
        Random rand = new Random();
        Integer password = rand.nextInt(40);
        return password.toString();
    }
}
