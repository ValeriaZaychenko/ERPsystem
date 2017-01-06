package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ErrorKeys;
import erp.controller.constants.ViewNames;
import erp.dto.HolidayDto;
import erp.dto.ProgressDto;
import erp.dto.UserDto;
import erp.exceptions.DateNotUniqueException;
import erp.exceptions.EntityNotFoundException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private String holidayId;
    private UserDto userDto;
    private ProgressDto progressDto;
    private HolidayDto holidayDto;
    private List<ProgressDto> progressDtos;
    private List<HolidayDto> holidayDtos;

    @Before
    public void setup() {
        progressDtos = new ArrayList<>();
        holidayDtos = new ArrayList<>();

        userId = UUID.randomUUID().toString();
        holidayId = UUID.randomUUID().toString();

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

        holidayDto = new HolidayDto();
        holidayDto.setId(holidayId);
        holidayDto.setDate(LocalDate.of(2016, 3, 8));
        holidayDto.setDescription("International women day");

        holidayDtos.add(holidayDto);

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
                getWorkingDaysQuantityBetweenDates(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 31)))
                .thenReturn(22);
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
                .andExpect(model().attribute(AttributeNames.ProgressView.monthDate, "1/2017"))
                ;

        verify(mockReportService, times(1))
                .getAllUsersWorkingTimeBetweenDates(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 31));
        verify(mockDayCounterService, times(1))
                .countWeekendsBetweenDates(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 31));
        verify(mockDayCounterService, times(1))
                .countHolidaysBetweenDates(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 31));
        verify(mockDayCounterService, times(1))
                .getWorkingDaysQuantityBetweenDates(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 31));
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
                getWorkingDaysQuantityBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31)))
                .thenReturn(22);
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
                .andExpect(model().attribute(AttributeNames.ProgressView.monthDate, "1/2016"))
                ;

        verify(mockReportService, times(1))
                .getAllUsersWorkingTimeBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31));
        verify(mockDayCounterService, times(1))
                .countWeekendsBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31));
        verify(mockDayCounterService, times(1))
                .countHolidaysBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31));
        verify(mockDayCounterService, times(1))
                .getWorkingDaysQuantityBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31));
        verify(mockDayCounterService, times(1))
                .getAllDaysQuantityBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31));
    }

    @Test
    public void getHolidaysDefault() throws Exception {
        when(mockDayCounterService.findHolidaysOfYear(LocalDate.now().getYear()))
                .thenReturn(holidayDtos);

        this.mockMvc.perform(
                get("/holidays")
        )
                .andExpect(status().isOk())
                .andExpect(model().attribute(AttributeNames.ProgressView.holiday, holidayDtos))
                .andExpect(view().name(ViewNames.HOLIDAY.holiday))
                ;

        verify(mockDayCounterService, times(1))
                .findHolidaysOfYear(LocalDate.now().getYear());
    }

    @Test
    public void getHolidays() throws Exception {
        when(mockDayCounterService.findHolidaysOfYear(2016))
                .thenReturn(holidayDtos);

        this.mockMvc.perform(
                get("/holidays")
                .param("year", "2016")
        )
                .andExpect(status().isOk())
                .andExpect(model().attribute(AttributeNames.ProgressView.holiday, holidayDtos))
                .andExpect(view().name(ViewNames.HOLIDAY.holiday))
        ;

        verify(mockDayCounterService, times(1))
                .findHolidaysOfYear(2016);
    }

    @Test
    public void createHoliday() throws Exception {
        this.mockMvc.perform(
                post("/holidays/add")
                        .param("date", holidayDto.getDate().toString())
                        .param("description", holidayDto.getDescription())

        )
                .andExpect(status().isOk())
        ;

        verify(mockDayCounterService, only())
                .createHoliday(
                        holidayDto.getDate(),
                        holidayDto.getDescription()
                );
    }

    @Test
    public void createHolidayNotUnique() throws Exception {
        doThrow(new DateNotUniqueException(holidayDto.getDate()))
                .when(mockDayCounterService).
                createHoliday(holidayDto.getDate(), holidayDto.getDescription());

        this.mockMvc.perform(
                post("/holidays/add")
                        .param("date", holidayDto.getDate().toString())
                        .param("description", holidayDto.getDescription())

        )
                .andExpect(view().name("error"))
                .andExpect( (result) ->
                        result.getResponse().getContentAsString().contains(ErrorKeys.DateNotUniqueMessage)
                );
    }

    @Test
    public void editHoliday() throws Exception {
        this.mockMvc.perform(
                post("/holidays/edit")
                        .param("holidayId", holidayId)
                        .param("date", holidayDto.getDate().toString())
                        .param("description", holidayDto.getDescription())

        )
                .andExpect(status().isOk())
        ;

        verify(mockDayCounterService, only())
                .editHoliday(
                        holidayId,
                        holidayDto.getDate(),
                        holidayDto.getDescription()
                );
    }

    @Test
    public void editHolidayNotFound() throws Exception {
        doThrow(new EntityNotFoundException(ErrorKeys.EntityNotFoundMessage))
                .when(mockDayCounterService).
                editHoliday(holidayId, holidayDto.getDate(), holidayDto.getDescription());

        this.mockMvc.perform(
                post("/holidays/edit")
                        .param("holidayId", holidayId)
                        .param("date", holidayDto.getDate().toString())
                        .param("description", holidayDto.getDescription())

        )
                .andExpect(view().name("error"))
                .andExpect( (result) ->
                        result.getResponse().getContentAsString().contains(ErrorKeys.EntityNotFoundMessage))
        ;
    }

    @Test
    public void editHolidayDateNotUnique() throws Exception {
        doThrow(new DateNotUniqueException(holidayDto.getDate()))
                .when(mockDayCounterService).
                editHoliday(holidayId, holidayDto.getDate(), holidayDto.getDescription());

        this.mockMvc.perform(
                post("/holidays/edit")
                        .param("holidayId", holidayId)
                        .param("date", holidayDto.getDate().toString())
                        .param("description", holidayDto.getDescription())

        )
                .andExpect(view().name("error"))
                .andExpect( (result) ->
                        result.getResponse().getContentAsString().contains(ErrorKeys.DateNotUniqueMessage))
        ;
    }

    @Test
    public void deleteHoliday() throws Exception {
        this.mockMvc.perform(
                post("/holidays/delete")
                        .param("holidayId", holidayId)
        )
                .andExpect(status().isOk()
                );

        verify(mockDayCounterService, only())
                .deleteHoliday(holidayId);
    }

    @Test
    public void deleteHolidayNotFound() throws Exception {
        doThrow(new EntityNotFoundException(holidayId))
                .when(mockDayCounterService).
                deleteHoliday(holidayId);

        this.mockMvc.perform(
                post("/holidays/delete")
                        .param("holidayId", holidayId)
        )
                .andExpect(view().name("error"))
                .andExpect( (result) ->
                        result.getResponse().getContentAsString().contains(ErrorKeys.EntityNotFoundMessage)
                );
    }

    @Test
    public void cloneHoliday() throws Exception {
        this.mockMvc.perform(
                post("/holidays/holiday/clone")
                        .param("holidayId", holidayId)
        )
                .andExpect(status().isOk()
                );

        verify(mockDayCounterService, only())
                .copyHolidayToNextYear(holidayId);
    }

    @Test
    public void cloneHolidayDateNotUnique() throws Exception {
        doThrow(new DateNotUniqueException(holidayDto.getDate()))
                .when(mockDayCounterService).
                copyHolidayToNextYear(holidayId);

        this.mockMvc.perform(
                post("/holidays/holiday/clone")
                        .param("holidayId", holidayId)
        )
                .andExpect(view().name("error"))
                .andExpect( (result) ->
                        result.getResponse().getContentAsString().contains(ErrorKeys.DateNotUniqueMessage));
    }

    @Test
    public void cloneHolidays() throws Exception {
        this.mockMvc.perform(
                post("/holidays/clone")
                        .param("year", "2016")
        )
                .andExpect(status().isOk()
                );

        verify(mockDayCounterService, only())
                .copyYearHolidaysToNext(2016);
    }
}
