package erp.dto;


import java.io.Serializable;
import java.util.List;

public class TeamDto implements Serializable {

    private String id;
    private String name;
    private List<BriefUserDto> briefUserDtos;
    private BriefUserDto teamLeadBriefUserDto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BriefUserDto> getBriefUserDtos() {
        return briefUserDtos;
    }

    public void setBriefUserDtos(List<BriefUserDto> briefUserDtos) {
        this.briefUserDtos = briefUserDtos;
    }

    public BriefUserDto getTeamLeadBriefUserDto() {
        return teamLeadBriefUserDto;
    }

    public void setTeamLeadBriefUserDto(BriefUserDto teamLeadBriefUserDto) {
        this.teamLeadBriefUserDto = teamLeadBriefUserDto;
    }
}
