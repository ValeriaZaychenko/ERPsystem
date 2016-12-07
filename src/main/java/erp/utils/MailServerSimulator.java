package erp.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public final class MailServerSimulator {

    private static final String FILENAME = "C:\\Users\\lera\\IdeaProjects\\ERP\\src\\main\\resources\\first_login_passwords.txt";

    public static final BufferedWriter bw;
    static {
        try {
            bw = new BufferedWriter(new FileWriter(FILENAME, true));
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    public static void writeDataToFile(String content){
        System.out.println(content);
        try {
            bw.write(" ");
            bw.write(content);
            bw.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

