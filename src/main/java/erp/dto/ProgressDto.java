package erp.dto;


import java.io.Serializable;

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
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
