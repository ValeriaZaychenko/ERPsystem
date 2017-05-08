package erp.dto;


import java.io.Serializable;
import java.text.DecimalFormat;

public class ProgressDto implements Serializable  {

    private String userId;
    private double userActualHoursWorked;
    private double userExpectedHoursWorked;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getUserActualHoursWorked() {
        return userActualHoursWorked;
    }

    public void setUserActualHoursWorked(double userActualHoursWorked) {
        this.userActualHoursWorked = userActualHoursWorked;
    }

    public double getUserExpectedHoursWorked() {
        return userExpectedHoursWorked;
    }

    public void setUserExpectedHoursWorked(double userExpectedHoursWorked) {
        this.userExpectedHoursWorked = userExpectedHoursWorked;
    }

    public double getProgress() {
        double progress = userActualHoursWorked * 100.0 / userExpectedHoursWorked;
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(progress));
    }
}
