package erp.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "missedDays")
public class MissedDay {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true)
    private String id;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private MissedDayType missedDayType;

    @ManyToOne
    private User user;

    public MissedDay(LocalDate date, MissedDayType missedDayType, User user) {
        this.date = date;
        this.missedDayType = missedDayType;
        this.user = user;
    }

    protected MissedDay() {}

    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public MissedDayType getMissedDayType() {
        return missedDayType;
    }

    public void setMissedDayType(MissedDayType missedDayType) {
        this.missedDayType = missedDayType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
