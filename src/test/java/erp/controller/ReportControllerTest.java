package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ViewNames;
import erp.dto.ReportDto;
import erp.dto.UserDto;
import erp.exceptions.EntityNotFoundException;
import erp.service.IReportService;
import erp.utils.DateParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
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
        reportDto.setDate(DateParser.parseDate("2016-09-09"));
        reportDto.setWorkingTime(8);
        reportDto.setDescription("Issue 35");
        reportDto.setUserId(userDto.getId());
        reportDto.setRemote(true);

        this.mockMvc = MockMvcBuilders.standaloneSetup(theController)
                .setControllerAdvice(new ExceptionHandlingAdvice())
                .build();
    }

    @Test
    public void noUsersInListYet() throws Exception {
        when(mockReportService.viewUserReports(userDto.getId())).thenReturn(dtos);

        this.mockMvc.perform(
                get("/reports")
                    .principal(userDto)
       )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reports))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userReports, dtos))
        ;

        verify(mockReportService, times(1))
                .viewUserReports(userDto.getId())
        ;
    }

    @Test
    public void createReport() throws Exception {
        this.mockMvc.perform(
                post("/reports/add")
                        .principal(userDto)
                        .param("date", reportDto.getDate().toString())
                        .param("time", Integer.toString(reportDto.getWorkingTime()))
                        .param("description", reportDto.getDescription())
                        .param("remote", Boolean.toString(reportDto.isRemote()))

       )
                .andExpect(redirectedUrl("/reports"))
        ;

        verify(mockReportService, only())
                .createReport(
                        reportDto.getDate(),
                        reportDto.getWorkingTime(),
                        reportDto.getDescription(),
                        reportDto.getUserId(),
                        Boolean.toString(reportDto.isRemote())
               );
    }


    @Test
    public void editReport() throws Exception {
        this.mockMvc.perform(
                post("/reports/edit")
                        .param("reportId", reportId)
                        .param("date", reportDto.getDate().toString())
                        .param("time", Integer.toString(reportDto.getWorkingTime()))
                        .param("description", reportDto.getDescription())
                        .param("remote", Boolean.toString(reportDto.isRemote()))
       )
                .andExpect(redirectedUrl("/reports"))
        ;

        verify(mockReportService, only())
                .editReport(
                        reportId,
                        reportDto.getDate(),
                        reportDto.getWorkingTime(),
                        reportDto.getDescription(),
                        Boolean.toString(reportDto.isRemote())
               );
    }

    @Test
    public void deleteReport() throws Exception {
        this.mockMvc.perform(
                post("/reports/delete")
                        .param("reportId", reportId)
       )
                .andExpect(redirectedUrl("/reports")
               );

        verify(mockReportService, only())
                .removeReport(reportId);
    }

    @Test
    public void deleteReportNotFound() throws Exception {
        doThrow(new EntityNotFoundException(reportId))
                .when(mockReportService).
                removeReport(reportId);

        this.mockMvc.perform(
                post("/reports/delete")
                        .param("reportId", reportId)
        )
                .andExpect(view().name("error"))
                .andExpect(new ResultMatcher() {

                    @Override
                    public void match(MvcResult result) throws Exception {
                        result.getResponse().getContentAsString().contains("Database doesn't have entity with name");
                    }
                });
    }
}
