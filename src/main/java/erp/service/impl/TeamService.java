package erp.service.impl;

import erp.domain.Team;
import erp.dto.TeamDto;
import erp.exceptions.DuplicateNameException;
import erp.exceptions.EntityNotFoundException;
import erp.repository.TeamRepository;
import erp.repository.UserRepository;
import erp.service.ITeamService;
import erp.utils.DtoBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService implements ITeamService {

    @Inject
    private TeamRepository teamRepository;
    @Inject
    private UserRepository userRepository;

    @Transactional
    @Override
    public TeamDto createTeam(String name, List<String> userIds, String teamLeadId) {
        checkNameIsUnique(name);
        Team team = new Team(name, teamLeadId);
        userIds.stream().forEach((userId) -> team.addUserId(userId));
        teamRepository.save(team);
        return DtoBuilder.teamToDto(team, userRepository);
    }

    @Transactional
    @Override
    public TeamDto editTeam(String id, String name, List<String> userIds, String teamLeadId) {
        Team team = restoreTeamFromRepository(id);

        if(!team.getName().equals(name)) {
            checkNameIsUnique(name);
            team.setName(name);
        }

        Iterator i = team.getUserIds();
        while (i.hasNext()) {
            String oldUserId = (String) i.next();
            if (userIds.contains(oldUserId)) {
                userIds.remove(oldUserId);
            }
            else
                i.remove();
        }
        if(userIds.size() != 0)
            userIds.stream().forEach((userId) -> team.addUserId(userId));

        if(!team.getTeamLeadId().equals(teamLeadId))
            team.setTeamLeadId(teamLeadId);

        return DtoBuilder.teamToDto(team, userRepository);
    }

    @Transactional
    @Override
    public void removeTeam(String id) {
        Team team = restoreTeamFromRepository(id);
        teamRepository.delete(team);
    }

    @Transactional
    @Override
    public TeamDto findTeam(String id) {
        return DtoBuilder.teamToDto(restoreTeamFromRepository(id), userRepository);
    }

    @Transactional
    @Override
    public List<TeamDto> viewAllTeams() {
        return teamRepository.findAll().stream()
                .map(team -> DtoBuilder.teamToDto(team, userRepository))
                .collect(Collectors.toList());
    }

    private Team restoreTeamFromRepository(String id) {
        Team team = teamRepository.findOne(id);
        if (team == null)
            throw new EntityNotFoundException(Team.class.getName());
        return team;
    }

    private void checkNameIsUnique(String name) {
        if(!isNameUnique(name)) {
            throw new DuplicateNameException(name);
        }
    }

    private boolean isNameUnique(String name) {
        return (teamRepository.findByName(name) == null);
    }
}
