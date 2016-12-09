package erp.service;

public interface IPasswordService {

    byte[] getHashFromPassword(String password);
    boolean comparePasswords(String password, byte[] hashedPassword);
    String generatePassword();
}
