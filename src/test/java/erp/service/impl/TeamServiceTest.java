package erp.service.impl;

import erp.domain.UserRole;
import erp.dto.BriefUserDto;
import erp.dto.TeamDto;
import erp.exceptions.DuplicateNameException;
import erp.exceptions.EntityNotFoundException;
import erp.repository.TeamRepository;
import erp.service.ITeamService;
import erp.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
@Transactional()
@WithMockUser(username = "ram", authorities={"AUTH_ADMIN"})
public class TeamServiceTest {

    @Inject
    private IUserService userService;
    @Inject
    private ITeamService teamService;
    @Inject
    private TeamRepository teamRepository;

    @Inject
    private ApplicationContext context;

    @PersistenceContext
    private EntityManager entityManager;

    //---CREATE TEAM VALIDATION TESTS-----------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void createTeamNullFields() {
        teamService.createTeam(null, null, null);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createTeamBlankName() {
        teamService.createTeam("", new ArrayList<>(), "teamleadId");
    }

    //---CREATE TEAM LOGIC TESTS----------------------------------------------------------------------------------------

    @Test
    public void createTeamCorrectly() {
        String leadId = createSimpleUser();
        TeamDto teamDto = createTeamWithOneLeader(leadId);

        assertNotNull(teamDto.getId());
        assertEquals(teamDto.getName(), "Team A");
        assertEquals(teamDto.getBriefUserDtos().size(), 1);
        assertEquals(teamDto.getTeamLeadBriefUserDto().getId(), leadId);
    }

    @Test(expected = DuplicateNameException.class)
    public void createTeamNonUniqueName() {
        String leadId = createSimpleUser();
        createTeamWithOneLeader(leadId);
        createTeamWithOneLeader(leadId);
    }

    //---EDIT TEAM VALIDATION TESTS-------------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void editTeamNullId() {
        teamService.editTeam(null, "Bla bla", null, "XXX");
    }

    @Test(expected = ConstraintViolationException.class)
    public void editBlankNameUser() {
        String userId = createSimpleUser();
        TeamDto dto = createTeamWithOneLeader(userId);
        List<String> ids = dto.getBriefUserDtos().stream()
                                .map(userDto -> userDto.getId())
                                .collect(Collectors.toList());

        teamService.editTeam(dto.getId(), "", ids, userId);
    }

    @Test(expected = DuplicateNameException.class)
    public void editNonUniqueNameUser() {
        String userId = createSimpleUser();
        TeamDto dto0 = createTeamWithOneLeader(userId);
        TeamDto dto1 = createAnotherTeamWithOneLeader(userId);
        List<String> ids = dto0.getBriefUserDtos().stream()
                .map(userDto -> userDto.getId())
                .collect(Collectors.toList());

        teamService.editTeam(dto0.getId(), dto1.getName(), ids, userId);
    }

    //---EDIT USER LOGIC TESTS------------------------------------------------------------------------------------------

    @Test
    public void editNameCorrectly() {
        String userId = createSimpleUser();
        TeamDto dto = createTeamWithOneLeader(userId);

        List<String> ids = dto.getBriefUserDtos().stream()
                .map(userDto -> userDto.getId())
                .collect(Collectors.toList());

        TeamDto newTeamDto = teamService.editTeam(dto.getId(), "New name", ids, dto.getTeamLeadBriefUserDto().getId());

        assertEquals(newTeamDto.getName(), "New name");
    }

    @Test
    public void editTeamLeadCorrectly() {
        String userId = createSimpleUser();
        TeamDto dto = createTeamWithOneLeader(userId);
        String newUserId = createAnotherSimpleUser();

        List<String> ids = dto.getBriefUserDtos().stream()
                .map(userDto -> userDto.getId())
                .collect(Collectors.toList());

        TeamDto newTeamDto = teamService.editTeam(dto.getId(), dto.getName(), ids, newUserId);

        assertEquals(newTeamDto.getTeamLeadBriefUserDto().getId(), newUserId);
    }

    @Test
    public void editUserIdsCorrectly() {
        String userId0 = createSimpleUser();
        String userId1 = createAnotherSimpleUser();
        List<String> userIds = new ArrayList<>();
        userIds.add(userId0);
        userIds.add(userId1);

        TeamDto dto = createTeamWithUserIdsList(userIds, userId0);
        String userId2 = userService.createUser("Alex", "alex@mail.ru", UserRole.USER.toString());
        userIds.add(userId2);

        TeamDto newTeamDto = teamService.editTeam(dto.getId(), dto.getName(), userIds, userId0);

        List<BriefUserDto> oldDtos = dto.getBriefUserDtos();
        List<BriefUserDto> newDtos = newTeamDto.getBriefUserDtos();

        assertEquals(newDtos.get(0).getId(), oldDtos.get(0).getId());
    }

    @Test
    public void editUserIdsDeleteOldCorrectly() {
        String userId0 = createSimpleUser();
        String userId1 = createAnotherSimpleUser();
        String userId2 = userService.createUser("Alex", "alex@mail.ru", UserRole.USER.toString());
        List<String> userIds = new ArrayList<>();
        userIds.add(userId0);
        userIds.add(userId1);
        userIds.add(userId2);

        TeamDto dto = createTeamWithUserIdsList(userIds, userId0);
        userIds.remove(userId2);

        TeamDto newTeamDto = teamService.editTeam(dto.getId(), dto.getName(), userIds, userId0);

        List<BriefUserDto> oldDtos = dto.getBriefUserDtos();
        List<BriefUserDto> newDtos = newTeamDto.getBriefUserDtos();

        assertEquals(newDtos.size(), 2);
    }

    //---REMOVE USER VALIDATION TESTS-----------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void deleteTeamNullId() {
        teamService.removeTeam(null);
    }

    //---REMOVE USER LOGIC TESTS----------------------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void deleteTeamFromEmptyService() {
        teamService.removeTeam(UUID.randomUUID().toString());
    }

    @Test
    public void deleteTeamCorrectly() {
        String leadId = createSimpleUser();
        TeamDto teamDto0 = createTeamWithOneLeader(leadId);
        TeamDto teamDto1 = createAnotherTeamWithOneLeader(leadId);

        teamService.removeTeam(teamDto0.getId());

        assertEquals(teamRepository.findAll().size(), 1);
        assertNull(teamRepository.findOne(teamDto0.getId()));
        assertNotNull(teamRepository.findOne(teamDto1.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteTeamTwice() {
        String id = createSimpleUser();
        TeamDto teamDto = createTeamWithOneLeader(id);

        teamService.removeTeam(teamDto.getId());
        teamService.removeTeam(teamDto.getId());
    }

    //---FIND TEAM TESTS------------------------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void findTeamNullId() {
        teamService.findTeam(null);
    }

    @Test(expected = EntityNotFoundException.class)
    public void findTeamRandomId() {
        teamService.findTeam(UUID.randomUUID().toString());
    }

    @Test
    public void findTeamCorrectly() {
        String id = createSimpleUser();
        TeamDto dto = createTeamWithOneLeader(id);

        TeamDto foundDto = teamService.findTeam(dto.getId());

        assertEquals(dto.getId(), foundDto.getId());
        assertEquals(dto.getName(), foundDto.getName());
        assertEquals(dto.getTeamLeadBriefUserDto(), dto.getTeamLeadBriefUserDto());
        assertEquals(dto.getBriefUserDtos(), dto.getBriefUserDtos());
    }

    //---VIEW ALL TEAMS TESTS------------------------------------------------------------------------------------------------

    @Test
    public void viewAllTeamsEmptyService() {
        assertEquals(teamService.viewAllTeams().size(), 0);
    }

    @Test
    public void viewAllTeamsCorrectly() {
        assertEquals(teamService.viewAllTeams().size(), 0);

        String id = createSimpleUser();
        createTeamWithOneLeader(id);
        createAnotherTeamWithOneLeader(id);

        assertEquals(teamService.viewAllTeams().size(), 2);
    }

    //------------------------------------------------------------------------------------------------------------------

    private String createSimpleUser() {
        return userService.createUser("name", "email@mail.ru", UserRole.LEADER.toString());
    }

    private String createAnotherSimpleUser() {
        return userService.createUser("name", "new_email@mail.ru", UserRole.LEADER.toString());
    }

    private TeamDto createTeamWithOneLeader(String userId) {
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        return teamService.createTeam("Team A", userIds, userId);
    }

    private TeamDto createTeamWithUserIdsList(List<String> userIds, String teamLeadId) {
        return teamService.createTeam("Team X", userIds, teamLeadId);
    }

    private TeamDto createAnotherTeamWithOneLeader(String userId) {
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        return teamService.createTeam("Team B", userIds, userId);
    }
}
