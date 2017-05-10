package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ErrorKeys;
import erp.controller.constants.ViewNames;
import erp.dto.CalendarDto;
import erp.dto.HolidayDto;
import erp.dto.ProgressDto;
import erp.dto.UserDto;
import erp.exceptions.DateNotUniqueException;
import erp.exceptions.EntityNotFoundException;
import erp.service.ICalendarService;
import erp.service.IHolidayService;
import erp.service.IProgressService;
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
    private IHolidayService holidayService;
    @Mock
    private ICalendarService calendarService;
    @Mock
    private IProgressService mockProgressService;
    @InjectMocks
    private ProgressController theController;

    private MockMvc mockMvc;
    private String userId;
    private String holidayId;
    private UserDto userDto;
    private ProgressDto progressDto;
    private HolidayDto holidayDto;
    private CalendarDto calendarDto;
    private List<ProgressDto> progressDtos;
    private List<HolidayDto> holidayDtos;

    @Before
    public void setup() {
        progressDtos = new ArrayList<>();
        holidayDtos = new ArrayList<>();

        userId = UUID.randomUUID().toString();
        holidayId = UUID.randomUUID().toString();

        //Create User Dto
        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("Oleg");
        userDto.setEmail("Olegov");
        userDto.setUserRole("USER");

        //Create Progress Dto
        progressDto = new ProgressDto();
        progressDto.setUserId(userId);
        progressDto.setUserActualHoursWorked(16.0);
        progressDto.setUserExpectedHoursWorked(30.0);

        progressDtos.add(progressDto);

        //Create holiday Dto
        holidayDto = new HolidayDto();
        holidayDto.setId(holidayId);
        holidayDto.setDate(LocalDate.of(2016, 3, 8));
        holidayDto.setDescription("International women day");

        holidayDtos.add(holidayDto);

        //Create Calendar Dto
        calendarDto = new CalendarDto();
        calendarDto.setWeekends(2);
        calendarDto.setHolidays(1);
        calendarDto.setWorkdays(2);
        calendarDto.setAllDays(5);

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
        when(mockProgressService.
                getAllUsersProgressBetweenDates(
                        LocalDate.of(2017, LocalDate.now().getMonth(), 1),
                        LocalDate.of(2017, LocalDate.now().getMonth(), 31)))
                .thenReturn(progressDtos);
        when(calendarService.
                getCalendarInformationBetweenDates(
                        LocalDate.of(2017, LocalDate.now().getMonth(), 1),
                        LocalDate.of(2017, LocalDate.now().getMonth(), 31)))
                .thenReturn(calendarDto);

        this.mockMvc.perform(
                get("/progress")
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.PROGRESS.progress))
                .andExpect(model().attribute(AttributeNames.ProgressView.progress, progressDtos))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
                .andExpect(model().attribute(AttributeNames.ProgressView.monthDate,
                        String.valueOf(LocalDate.now().getMonth().getValue()) + "/2017"))
                ;

        verify(mockProgressService, times(1))
                .getAllUsersProgressBetweenDates(
                        LocalDate.of(2017, LocalDate.now().getMonth(), 1),
                        LocalDate.of(2017, LocalDate.now().getMonth(), 31));
        verify(calendarService, times(1))
                .getCalendarInformationBetweenDates(
                        LocalDate.of(2017, LocalDate.now().getMonth(), 1),
                        LocalDate.of(2017, LocalDate.now().getMonth(), 31));
    }

    @Test
    public void viewProgressBetweenDates() throws Exception {
        when(mockProgressService.
                getAllUsersProgressBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31)))
                .thenReturn(progressDtos);
        when(calendarService.
                getCalendarInformationBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31)))
                .thenReturn(calendarDto);

        this.mockMvc.perform(
                get("/progress")
                        .param("month", "2016-01")
        )
                .andExpect(view().name(ViewNames.PROGRESS.progress))
                .andExpect(model().attribute(AttributeNames.ProgressView.progress, progressDtos))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
                .andExpect(model().attribute(AttributeNames.ProgressView.monthDate, "1/2016"))
                ;

        verify(mockProgressService, times(1))
                .getAllUsersProgressBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31));
        verify(calendarService, times(1))
                .getCalendarInformationBetweenDates(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31));
    }

    @Test
    public void getHolidaysDefault() throws Exception {
        when(holidayService.findHolidaysOfYear(LocalDate.now().getYear()))
                .thenReturn(holidayDtos);

        this.mockMvc.perform(
                get("/holidays")
        )
                .andExpect(status().isOk())
                .andExpect(model().attribute(AttributeNames.ProgressView.holiday, holidayDtos))
                .andExpect(view().name(ViewNames.HOLIDAY.holiday))
                ;

        verify(holidayService, times(1))
                .findHolidaysOfYear(LocalDate.now().getYear());
    }

    @Test
    public void getHolidays() throws Exception {
        when(holidayService.findHolidaysOfYear(2016))
                .thenReturn(holidayDtos);

        this.mockMvc.perform(
                get("/holidays")
                .param("year", "2016")
        )
                .andExpect(status().isOk())
                .andExpect(model().attribute(AttributeNames.ProgressView.holiday, holidayDtos))
                .andExpect(view().name(ViewNames.HOLIDAY.holiday))
        ;

        verify(holidayService, times(1))
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

        verify(holidayService, only())
                .createHoliday(
                        holidayDto.getDate(),
                        holidayDto.getDescription()
                );
    }

    @Test
    public void createHolidayNotUnique() throws Exception {
        doThrow(new DateNotUniqueException(holidayDto.getDate()))
                .when(holidayService).
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

        verify(holidayService, only())
                .editHoliday(
                        holidayId,
                        holidayDto.getDate(),
                        holidayDto.getDescription()
                );
    }

    @Test
    public void editHolidayNotFound() throws Exception {
        doThrow(new EntityNotFoundException(ErrorKeys.EntityNotFoundMessage))
                .when(holidayService).
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
                .when(holidayService).
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

        verify(holidayService, only())
                .deleteHoliday(holidayId);
    }

    @Test
    public void deleteHolidayNotFound() throws Exception {
        doThrow(new EntityNotFoundException(holidayId))
                .when(holidayService).
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

        verify(holidayService, only())
                .copyHolidayToNextYear(holidayId);
    }

    @Test
    public void cloneHolidayDateNotUnique() throws Exception {
        doThrow(new DateNotUniqueException(holidayDto.getDate()))
                .when(holidayService).
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

        verify(holidayService, only())
                .copyYearHolidaysToNext(2016);
    }
}
