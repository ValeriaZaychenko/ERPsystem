package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.SessionKeys;
import erp.controller.constants.ViewNames;
import erp.dto.ReportDto;
import erp.dto.UserDto;
import erp.service.IReportService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith( MockitoJUnitRunner.class )
public class ReportControllerTest {

    @Mock
    private IReportService mockReportService;
    @InjectMocks
    private ReportController theController;

    private MockMvc mockMvc;
    private String reportId;
    private ReportDto reportDto;
    private List<ReportDto> dtos;
    private UserDto userDto;

    @Before
    public void setup() {
        dtos = new ArrayList<>();

        userDto = new UserDto();
        userDto.setId(UUID.randomUUID().toString());
        userDto.setName("Pet");
        userDto.setEmail("p@mail.rt");
        userDto.setUserRole("USER");

        reportId = UUID.randomUUID().toString();

        reportDto = new ReportDto();
        reportDto.setId(reportId);
        reportDto.setDate("2016-09-09");
        reportDto.setWorkingTime(8);
        reportDto.setDescription("Issue 35");
        reportDto.setUserId(userDto.getId());

        this.mockMvc = MockMvcBuilders.standaloneSetup( theController ).build();
    }

    @Test
    public void noUsersInListYet() throws Exception {
        when(mockReportService.viewUserReports(userDto.getId())).thenReturn(dtos);

        this.mockMvc.perform(
                get("/reports")
                        .sessionAttr( SessionKeys.USER.user, userDto )
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reports))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userReports, dtos))
        ;

        verify(mockReportService, times(1))
                .viewUserReports(userDto.getId())
        ;
    }
}
