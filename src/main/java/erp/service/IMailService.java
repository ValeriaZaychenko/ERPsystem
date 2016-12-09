package erp.service;

public interface IMailService {

    void writeDataToFile(String content);
    String getLastContent();
}
