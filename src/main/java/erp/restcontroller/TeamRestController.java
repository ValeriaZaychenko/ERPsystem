package erp.restcontroller;

import erp.dto.TeamDto;
import erp.service.ITeamService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/teams")
public class TeamRestController {

    @Inject
    private ITeamService teamService;

    @RequestMapping(method = RequestMethod.GET)
    public List<TeamDto> findAll() {
        return teamService.viewAllTeams();
    }

    @RequestMapping(method = RequestMethod.POST)
    public TeamDto add(@RequestBody TeamDto team) {
        List<String> ids = team.getBriefUserDtos().stream()
                .map(userDto -> userDto.getId())
                .collect(Collectors.toList());
        return teamService.createTeam(team.getName(), ids, team.getTeamLeadBriefUserDto().getId());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TeamDto findOne(@PathVariable String id) {
        return teamService.findTeam(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TeamDto update(@PathVariable String id, @RequestBody TeamDto team) {
        List<String> ids = team.getBriefUserDtos().stream()
                .map(userDto -> userDto.getId())
                .collect(Collectors.toList());
        return teamService.editTeam(id, team.getName(), ids, team.getTeamLeadBriefUserDto().getId());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void remove(@PathVariable String id) {
        teamService.removeTeam(id);
    }
}
