package erp.dto;


import java.io.Serializable;
import java.text.DecimalFormat;

public class ProgressDto implements Serializable  {

    private String userId;
    private String userName;
    private double userCurrentMonthWorkingTime;
    private double progress;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getUserCurrentMonthWorkingTime() {
        return userCurrentMonthWorkingTime;
    }

    public void setUserCurrentMonthWorkingTime(double userCurrentMonthWorkingTime) {
        this.userCurrentMonthWorkingTime = userCurrentMonthWorkingTime;
    }

    public double getProgress() {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(progress));
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
