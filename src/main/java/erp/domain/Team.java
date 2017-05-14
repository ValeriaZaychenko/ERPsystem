package erp.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true)
    private String id;
    private String name;
    @ElementCollection
    private List<String> userIds;
    private String teamLeadId;

    public Team(String name, String teamLeadId) {
        this.name = name;
        this.userIds = new ArrayList<>();
        this.teamLeadId = teamLeadId;
    }

    protected Team(){}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Iterator<String> getUserIds() {
        return userIds.listIterator();
    }

    public void addUserId(String id) {
        this.userIds.add(id);
    }

    public void removeUserId(String id) {
        this.userIds.remove(id);
    }

    public String getTeamLeadId() {
        return teamLeadId;
    }

    public void setTeamLeadId(String teamLead) {
        this.teamLeadId = teamLead;
    }
}
