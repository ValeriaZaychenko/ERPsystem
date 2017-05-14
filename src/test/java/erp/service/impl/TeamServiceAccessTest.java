package erp.service.impl;

import erp.domain.Team;
import erp.domain.User;
import erp.domain.UserRole;
import erp.dto.TeamDto;
import erp.service.IPasswordService;
import erp.service.ITeamService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
@Transactional()
@WithMockUser(username = "user", authorities={"AUTH_USER"})
public class TeamServiceAccessTest {

    @Inject
    private ITeamService teamService;
    @Inject
    private IPasswordService passwordService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test(expected = AccessDeniedException.class)
    public void createTeam() {
        createTeamWithOneLeader(createSimpleUser());
    }

    @Test(expected = AccessDeniedException.class)
    public void editTeam() {
        List<String> ids = new ArrayList<>();
        String userId = createSimpleUser();
        ids.add(userId);
        String teamId = createSimpleTeam();

        teamService.editTeam(teamId, "new Name", ids, userId);
    }

    @Test(expected = AccessDeniedException.class)
    public void deleteTeam() {
        String teamId = createSimpleTeam();
        teamService.removeTeam(teamId);
    }

    @Test
    public void findTeam() {
        String teamId = createSimpleTeam();
        teamService.findTeam(teamId);
    }

    @Test
    public void viewAllTeams() {
        createSimpleTeam();
        teamService.viewAllTeams();
    }

    private String createSimpleUser() {
        User user = new User(
                "Petya",
                "p@mail",
                UserRole.USER,
                passwordService.getHashFromPassword(passwordService.generatePassword())
        );

        entityManager.persist(user);

        return user.getId();
    }

    private String createSimpleTeam() {
        String id = createSimpleUser();

        Team team = new Team("Team A", id);
        team.addUserId(id);

        entityManager.persist(team);

        return team.getId();
    }

    private TeamDto createTeamWithOneLeader(String userId) {
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        return teamService.createTeam("Team A", userIds, userId);
    }

}
