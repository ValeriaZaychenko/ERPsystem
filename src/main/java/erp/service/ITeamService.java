package erp.service;


import erp.dto.TeamDto;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface ITeamService {

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    TeamDto createTeam(
            @NotBlank String name,
            List<String> userIds,
            String teamLeadId);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    TeamDto editTeam(
            @NotNull String id,
            @NotBlank String name,
            List<String> userIds,
            String teamLeadId);

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    void removeTeam(@NotNull String id);

    TeamDto findTeam(@NotNull String id);

    List<TeamDto> viewAllTeams();
}
