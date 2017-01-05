package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ViewNames;
import erp.dto.ProgressDto;
import erp.dto.UserDto;
import erp.service.IDayCounterService;
import erp.service.IReportService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ProgressControllerTest {

    @Mock
    private IReportService mockReportService;
    @Mock
    private IDayCounterService mockDayCounterService;

    @InjectMocks
    private ProgressController theController;

    private MockMvc mockMvc;
    private String userId;
    private UserDto userDto;
    private ProgressDto progressDto;
    private List<ProgressDto> progressDtos;

    @Before
    public void setup() {
        progressDtos = new ArrayList<>();

        userId = UUID.randomUUID().toString();

        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("Oleg");
        userDto.setEmail("Olegov");
        userDto.setUserRole("USER");

        progressDto = new ProgressDto();
        progressDto.setUserId(userId);
        progressDto.setUserName(userDto.getName());
        progressDto.setUserCurrentMonthWorkingTime(16.0);
        progressDto.setProgress(30.0);

        progressDtos.add(progressDto);

        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setSuffix(".jsp");


        this.mockMvc = MockMvcBuilders.standaloneSetup(theController)
                .setControllerAdvice(new ExceptionHandlingAdvice())
                .setViewResolvers(resolver)
                .build();
    }

    @Test
    public void viewProgressDefault() throws Exception {
        when(mockReportService.
                getAllUsersWorkingTimeBetweenDates(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 31)))
                .thenReturn(progressDtos);
        when(mockDayCounterService.
                countWeekendsBetweenDates(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 31)))
                .thenReturn(9);
        when(mockDayCounterService.
                countHolidaysBetweenDates(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 31)))
                .thenReturn(0);
        when(mockDayCounterService.
                getAllDaysQuantityBetweenDates(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 31)))
                .thenReturn(31);

        this.mockMvc.perform(
                get("/progress")
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.PROGRESS.progress))
                .andExpect(model().attribute(AttributeNames.ProgressView.progress, progressDtos))
                .andExpect(model().attribute(AttributeNames.ProgressView.weekends, 9))
                .andExpect(model().attribute(AttributeNames.ProgressView.holiday, 0))
                .andExpect(model().attribute(AttributeNames.ProgressView.allDays, 31))
                .andExpect(model().attribute(AttributeNames.ProgressView.monthName, "JANUARY"))
                ;

        verify(mockReportService, times(1))
                .getAllUsersWorkingTimeBetweenDates(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 31));
        verify(mockDayCounterService, times(1))
                .countWeekendsBetweenDates(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 31));
        verify(mockDayCounterService, times(1))
                .countHolidaysBetweenDates(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 31));
        verify(mockDayCounterService, times(1))
                .getAllDaysQuantityBetweenDates(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 31));
    }

    @Test
    public void viewProgressBetweenDates() throws Exception {
        when(mockReportService.
                getAllUsersWorkingTimeBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31)))
                .thenReturn(progressDtos);
        when(mockDayCounterService.
                countWeekendsBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31)))
                .thenReturn(8);
        when(mockDayCounterService.
                countHolidaysBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31)))
                .thenReturn(1);
        when(mockDayCounterService.
                getAllDaysQuantityBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31)))
                .thenReturn(31);

        this.mockMvc.perform(
                get("/progress")
                        .param("month", "2016-01")
        )
                .andExpect(view().name(ViewNames.PROGRESS.progress))
                .andExpect(model().attribute(AttributeNames.ProgressView.progress, progressDtos))
                .andExpect(model().attribute(AttributeNames.ProgressView.weekends, 8))
                .andExpect(model().attribute(AttributeNames.ProgressView.holiday, 1))
                .andExpect(model().attribute(AttributeNames.ProgressView.allDays, 31))
                .andExpect(model().attribute(AttributeNames.ProgressView.monthName, "JANUARY"))
                ;

        verify(mockReportService, times(1))
                .getAllUsersWorkingTimeBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31));
        verify(mockDayCounterService, times(1))
                .countWeekendsBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31));
        verify(mockDayCounterService, times(1))
                .countHolidaysBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31));
        verify(mockDayCounterService, times(1))
                .getAllDaysQuantityBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31));
    }
}
