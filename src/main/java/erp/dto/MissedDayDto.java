package erp.dto;


import java.io.Serializable;
import java.time.LocalDate;

public class MissedDayDto implements Serializable {

    private String id;
    private LocalDate date;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
