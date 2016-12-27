package erp.service.impl;

import erp.service.IMailService;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class MailService implements IMailService {

    private String lastContent;

    private static final String FILENAME = "src/main/resources/first_login_passwords.txt";


    @Override
    public void writeDataToFile(String content) {

        lastContent = content;

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME, true));
            bw.write(content);
            bw.write('\n');
            bw.flush();
            bw.close();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getLastContent() {
        return lastContent;
    }

}
