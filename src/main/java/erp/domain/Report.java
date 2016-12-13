package erp.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Report {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true)
    private String id;
    private LocalDate date;
    private int workingTime;
    private String description;

    @ManyToOne
    private User user;

    public Report(LocalDate date, int workingTime, String description, User user) {
        this.date = date;
        this.workingTime = workingTime;
        this.description = description;
        this.user = user;
    }

    public Report(){}

    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(int workingTime) {
        this.workingTime = workingTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
