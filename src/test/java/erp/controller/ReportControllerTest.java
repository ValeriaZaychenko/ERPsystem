package erp.controller;

import erp.controller.constants.AttributeNames;
import erp.controller.constants.ErrorKeys;
import erp.controller.constants.ViewNames;
import erp.dto.CalendarDto;
import erp.dto.ProgressDto;
import erp.dto.ReportDto;
import erp.dto.UserDto;
import erp.exceptions.EntityNotFoundException;
import erp.service.ICalendarService;
import erp.service.IProgressService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportControllerTest {

    @Mock
    private IReportService mockReportService;
    @Mock
    private ICalendarService mockCalendarService;
    @Mock
    private IProgressService mockProgressService;
    @InjectMocks
    private ReportController theController;

    private MockMvc mockMvc;
    private String reportId;
    private String reportIdToday1;
    private String reportIdToday2;
    private String anotherDay;
    private ReportDto reportDto;
    private ReportDto reportDtoToday1;
    private ReportDto reportDtoToday2;
    private ReportDto reportDtoAnotherDay;
    private List<ReportDto> dtos;
    private UserDto userDto;
    private ProgressDto progressDto;
    private CalendarDto calendarDto;

    @Before
    public void setup() {
        dtos = new ArrayList<>();

        //Create user dto
        userDto = new UserDto();
        userDto.setId(UUID.randomUUID().toString());
        userDto.setName("Pet");
        userDto.setEmail("p@mail.rt");
        userDto.setUserRole("USER");

        //Create simple report in past
        reportId = UUID.randomUUID().toString();
        reportDto = new ReportDto();
        reportDto.setId(reportId);
        reportDto.setDate(DateParser.parseDate("2016-09-09"));
        reportDto.setDuration(8);
        reportDto.setDescription("Issue 35");
        reportDto.setUserId(userDto.getId());
        reportDto.setRemote(true);

        dtos.add(reportDto);

        //Today's 1 report
        reportIdToday1 = UUID.randomUUID().toString();
        reportDtoToday1 = new ReportDto();
        reportDtoToday1.setId(reportIdToday1);
        reportDtoToday1.setDate(LocalDate.now());
        reportDtoToday1.setDuration(8);
        reportDtoToday1.setDescription("Issue 111");
        reportDtoToday1.setUserId(userDto.getId());
        reportDtoToday1.setRemote(true);

        //Today's 2 report
        reportIdToday2 = UUID.randomUUID().toString();
        reportDtoToday2 = new ReportDto();
        reportDtoToday2.setId(reportIdToday2);
        reportDtoToday2.setDate(LocalDate.now());
        reportDtoToday2.setDuration(6);
        reportDtoToday2.setDescription("Issue 0000");
        reportDtoToday2.setUserId(userDto.getId());
        reportDtoToday2.setRemote(false);

        //Another day in past report
        anotherDay = UUID.randomUUID().toString();
        reportDtoAnotherDay = new ReportDto();
        reportDtoAnotherDay.setId(anotherDay);
        reportDtoAnotherDay.setDate(DateParser.parseDate("2016-09-20"));
        reportDtoAnotherDay.setDuration(6);
        reportDtoAnotherDay.setDescription("Issue 0000");
        reportDtoAnotherDay.setUserId(userDto.getId());
        reportDtoAnotherDay.setRemote(false);

        //Create progress
        progressDto = new ProgressDto();
        progressDto.setUserId(userDto.getId());
        progressDto.setUserActualHoursWorked(10.0);
        progressDto.setUserExpectedHoursWorked(50.0);

        //Calendar Dto for 1 day
        calendarDto = new CalendarDto();
        calendarDto.setWeekends(0);
        calendarDto.setHolidays(0);
        calendarDto.setWorkdays(1);
        calendarDto.setAllDays(1);

        this.mockMvc = MockMvcBuilders.standaloneSetup(theController)
                .setControllerAdvice(new ExceptionHandlingAdvice())
                .build();
    }

    @Test
    public void noUsersInListYet() throws Exception {
        when(mockReportService.viewUserReportsBetweenDates(
                        userDto.getId(), LocalDate.now(), LocalDate.now()))
                .thenReturn(dtos);
        when(mockProgressService.getUserProgressBetweenDates(
                userDto.getId(), LocalDate.now(), LocalDate.now()))
                .thenReturn(progressDto);
        when(mockCalendarService.getCalendarInformationBetweenDates(LocalDate.now(), LocalDate.now()))
                .thenReturn(calendarDto);

        this.mockMvc.perform(
                get("/reports")
                    .principal(userDto)
       )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reports))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userProgress,
                                             progressDto.getProgress()))
                .andExpect(model().attribute(AttributeNames.UserViewReports.sumOfDurations,
                                            progressDto.getUserActualHoursWorked()))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
        ;

        verify(mockReportService, times(1))
                .viewUserReportsBetweenDates(userDto.getId(), LocalDate.now(), LocalDate.now());
        verify(mockProgressService, times(2))
                .getUserProgressBetweenDates(userDto.getId(), LocalDate.now(), LocalDate.now());
        verify(mockCalendarService, times(1))
                .getCalendarInformationBetweenDates(LocalDate.now(), LocalDate.now());
    }

    @Test
    public void getUserReportsComponentCorrectlyWithoutFilter() throws Exception {
        when(mockReportService.viewUserReportsBetweenDates(
                userDto.getId(), LocalDate.now(), LocalDate.now())).thenReturn(dtos);
        when(mockProgressService.getUserProgressBetweenDates(
                userDto.getId(), LocalDate.now(), LocalDate.now()))
                .thenReturn(progressDto);
        when(mockCalendarService.getCalendarInformationBetweenDates(LocalDate.now(), LocalDate.now()))
                .thenReturn(calendarDto);

        this.mockMvc.perform(
                get("/reports/userReports")
                        .principal(userDto)
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reportsComponent))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userReports, dtos))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userProgress,
                        progressDto.getProgress()))
                .andExpect(model().attribute(AttributeNames.UserViewReports.sumOfDurations,
                        progressDto.getUserActualHoursWorked()))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
        ;

        verify(mockReportService, times(1))
                .viewUserReportsBetweenDates(userDto.getId(), LocalDate.now(), LocalDate.now());
        verify(mockProgressService, times(2))
                .getUserProgressBetweenDates(userDto.getId(), LocalDate.now(), LocalDate.now());
        verify(mockCalendarService, times(1))
                .getCalendarInformationBetweenDates(LocalDate.now(), LocalDate.now());
    }

    @Test
    public void getUserReportsComponentWithMonthFilter() throws Exception {
        when(mockReportService.viewUserReportsBetweenDates(
                userDto.getId(), LocalDate.of(2016, 11, 1), LocalDate.of(2016, 11, 30))).thenReturn(dtos);
        when(mockProgressService.getUserProgressBetweenDates(
                userDto.getId(), LocalDate.of(2016, 11, 1), LocalDate.of(2016, 11, 30)))
                .thenReturn(progressDto);
        when(mockCalendarService.getCalendarInformationBetweenDates(LocalDate.of(2016, 11, 1), LocalDate.of(2016, 11, 30)))
                .thenReturn(calendarDto);

        this.mockMvc.perform(
                get("/reports/userReports")
                        .param("filter", "2016-11")
                        .principal(userDto)
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reportsComponent))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userReports, dtos))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userProgress,
                        progressDto.getProgress()))
                .andExpect(model().attribute(AttributeNames.UserViewReports.sumOfDurations,
                        progressDto.getUserActualHoursWorked()))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
        ;

        verify(mockReportService, times(1))
                .viewUserReportsBetweenDates(userDto.getId(), LocalDate.of(2016, 11, 1), LocalDate.of(2016, 11, 30));
        verify(mockProgressService, times(2))
                .getUserProgressBetweenDates(userDto.getId(), LocalDate.of(2016, 11, 1), LocalDate.of(2016, 11, 30));
        verify(mockCalendarService, times(1))
                .getCalendarInformationBetweenDates(LocalDate.of(2016, 11, 1), LocalDate.of(2016, 11, 30));
    }

    @Test
    public void getUserReportsComponentWithFullDateFilter() throws Exception {
        when(mockReportService.viewUserReportsBetweenDates(
                userDto.getId(), LocalDate.of(2016, 11, 1), LocalDate.of(2016, 11, 1))).thenReturn(dtos);
        when(mockProgressService.getUserProgressBetweenDates(
                userDto.getId(), LocalDate.of(2016, 11, 1), LocalDate.of(2016, 11, 1)))
                .thenReturn(progressDto);
        when(mockCalendarService.getCalendarInformationBetweenDates(LocalDate.of(2016, 11, 1), LocalDate.of(2016, 11, 1)))
                .thenReturn(calendarDto);

        this.mockMvc.perform(
                get("/reports/userReports")
                        .param("filter", "2016-11-01")
                        .principal(userDto)

        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reportsComponent))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userReports, dtos))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userProgress,
                        progressDto.getProgress()))
                .andExpect(model().attribute(AttributeNames.UserViewReports.sumOfDurations,
                        progressDto.getUserActualHoursWorked()))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
        ;

        verify(mockReportService, times(1))
                .viewUserReportsBetweenDates(userDto.getId(),LocalDate.of(2016, 11, 1), LocalDate.of(2016, 11, 1));
        verify(mockProgressService, times(2))
                .getUserProgressBetweenDates(userDto.getId(), LocalDate.of(2016, 11, 1), LocalDate.of(2016, 11, 1));
        verify(mockCalendarService, times(1))
                .getCalendarInformationBetweenDates(LocalDate.of(2016, 11, 1), LocalDate.of(2016, 11, 1));
    }

    @Test
    public void getUserReportsComponentWithFilterMonthAndWithGroupingParseEnum() throws Exception {
        when(mockReportService.viewUserReportsBetweenDates(
                userDto.getId(), LocalDate.now(), LocalDate.now())).thenReturn(dtos);

        this.mockMvc.perform(
                get("/reports/userReports")
                        .param("groupBy", "BLA_BLA")
                        .principal(userDto)
        )
                .andExpect(view().name("error"))
                .andExpect( (result) ->
                        result.getResponse().getContentAsString().contains(ErrorKeys.UnknownGroupByMessage)
                );
    }

    @Test
    public void getUserReportsComponentWithFilterMonthAndWithGroupingInvalidEnum() throws Exception {
        when(mockReportService.viewUserReportsBetweenDates(
                userDto.getId(), LocalDate.now(), LocalDate.now())).thenReturn(dtos);

        this.mockMvc.perform(
                get("/reports/userReports")
                        .param("groupBy", "INVALID")
                        .principal(userDto)
        )
                .andExpect(view().name("error"))
                .andExpect( (result) ->
                        result.getResponse().getContentAsString().contains(ErrorKeys.ApplyGroupByMessage)
                );
    }

    @Test
    public void getUserReportsComponentWithFilterMonthAndWithGroupingByDateAsc() throws Exception {
        dtos.add(reportDtoAnotherDay);

        when(mockReportService.viewUserReportsBetweenDates(
                userDto.getId(), LocalDate.of(2016, 9, 1), LocalDate.of(2016, 9, 30))).thenReturn(dtos);
        when(mockProgressService.getUserProgressBetweenDates(
                userDto.getId(), LocalDate.of(2016, 9, 1), LocalDate.of(2016, 9, 30)))
                .thenReturn(progressDto);
        when(mockCalendarService.getCalendarInformationBetweenDates(LocalDate.of(2016, 9, 1), LocalDate.of(2016, 9, 30)))
                .thenReturn(calendarDto);

        MvcResult result = this.mockMvc.perform(
                get("/reports/userReports")
                        .param("filter", "2016-09")
                        .param("groupBy", "DATE_DIRECT_ORDER")
                        .principal(userDto)

        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reportsComponent))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userProgress,
                        progressDto.getProgress()))
                .andExpect(model().attribute(AttributeNames.UserViewReports.sumOfDurations,
                        progressDto.getUserActualHoursWorked()))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
                .andReturn();

        Collection<Object> model = result.getModelAndView().getModel().values();

        Object firstDtosArrayList = model.stream().findFirst().get();
        ArrayList<ReportDto> arraydtos = (ArrayList<ReportDto>) firstDtosArrayList;

        assertEquals(arraydtos.get(0), reportDto);
        assertEquals(arraydtos.get(1), reportDtoAnotherDay);
        verify(mockReportService, times(1))
                .viewUserReportsBetweenDates(userDto.getId(), LocalDate.of(2016, 9, 1), LocalDate.of(2016, 9, 30));
        verify(mockProgressService, times(2))
                .getUserProgressBetweenDates(userDto.getId(),  LocalDate.of(2016, 9, 1), LocalDate.of(2016, 9, 30));
        verify(mockCalendarService, times(1))
                .getCalendarInformationBetweenDates( LocalDate.of(2016, 9, 1), LocalDate.of(2016, 9, 30));
    }

    @Test
    public void getUserReportsComponentWithFilterMonthAndWithGroupingByDateDesc() throws Exception {
        dtos.add(reportDtoAnotherDay);

        when(mockReportService.viewUserReportsBetweenDates(
                userDto.getId(), LocalDate.of(2016, 9, 1), LocalDate.of(2016, 9, 30))).thenReturn(dtos);
        when(mockProgressService.getUserProgressBetweenDates(
                userDto.getId(), LocalDate.of(2016, 9, 1), LocalDate.of(2016, 9, 30)))
                .thenReturn(progressDto);
        when(mockCalendarService.getCalendarInformationBetweenDates(LocalDate.of(2016, 9, 1), LocalDate.of(2016, 9, 30)))
                .thenReturn(calendarDto);

        MvcResult result = this.mockMvc.perform(
                get("/reports/userReports")
                        .param("filter", "2016-09")
                        .param("groupBy", "DATE_REVERSE_ORDER")
                        .principal(userDto)

        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reportsComponent))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userProgress,
                        progressDto.getProgress()))
                .andExpect(model().attribute(AttributeNames.UserViewReports.sumOfDurations,
                        progressDto.getUserActualHoursWorked()))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
                .andReturn();

        Collection<Object> model = result.getModelAndView().getModel().values();

        Object firstDtosArrayList = model.stream().findFirst().get();
        ArrayList<ReportDto> arraydtos = (ArrayList<ReportDto>) firstDtosArrayList;

        assertEquals(arraydtos.get(0), reportDtoAnotherDay);
        assertEquals(arraydtos.get(1), reportDto);
        verify(mockReportService, times(1))
                .viewUserReportsBetweenDates(userDto.getId(),LocalDate.of(2016, 9, 1), LocalDate.of(2016, 9, 30));
        verify(mockProgressService, times(2))
                .getUserProgressBetweenDates(userDto.getId(),  LocalDate.of(2016, 9, 1), LocalDate.of(2016, 9, 30));
        verify(mockCalendarService, times(1))
                .getCalendarInformationBetweenDates( LocalDate.of(2016, 9, 1), LocalDate.of(2016, 9, 30));
    }

    @Test
    public void getUserReportsComponentWithoutFilterAndWithGroupingByDurationAsc() throws Exception {
        dtos.clear();

        dtos.add(reportDtoToday1);
        dtos.add(reportDtoToday2);

        LocalDate today = LocalDate.now();

        when(mockReportService.viewUserReportsBetweenDates(
                userDto.getId(), today, today)).thenReturn(dtos);
        when(mockProgressService.getUserProgressBetweenDates(
                userDto.getId(), today, today))
                .thenReturn(progressDto);
        when(mockCalendarService.getCalendarInformationBetweenDates(today, today))
                .thenReturn(calendarDto);

        MvcResult result = this.mockMvc.perform(
                get("/reports/userReports")
                        .param("groupBy", "DURATION_DIRECT_ORDER")
                        .principal(userDto)

        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reportsComponent))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userProgress,
                        progressDto.getProgress()))
                .andExpect(model().attribute(AttributeNames.UserViewReports.sumOfDurations,
                        progressDto.getUserActualHoursWorked()))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
                .andReturn();

        Collection<Object> model = result.getModelAndView().getModel().values();

        Object firstDtosArrayList = model.stream().findFirst().get();
        ArrayList<ReportDto> arraydtos = (ArrayList<ReportDto>) firstDtosArrayList;

        assertEquals(arraydtos.get(0), reportDtoToday2);
        assertEquals(arraydtos.get(1), reportDtoToday1);
        verify(mockReportService, times(1))
                .viewUserReportsBetweenDates(userDto.getId(), today, today);
        verify(mockProgressService, times(2))
                .getUserProgressBetweenDates(userDto.getId(), today, today);
        verify(mockCalendarService, times(1))
                .getCalendarInformationBetweenDates(today, today);
    }

    @Test
    public void getUserReportsComponentWithoutFilterAndWithGroupingByDurationDesc() throws Exception {
        dtos.clear();

        dtos.add(reportDtoToday1);
        dtos.add(reportDtoToday2);

        LocalDate today = LocalDate.now();

        when(mockReportService.viewUserReportsBetweenDates(
                userDto.getId(), today, today)).thenReturn(dtos);
        when(mockProgressService.getUserProgressBetweenDates(
                userDto.getId(), today, today))
                .thenReturn(progressDto);
        when(mockCalendarService.getCalendarInformationBetweenDates(today, today))
                .thenReturn(calendarDto);

        MvcResult result = this.mockMvc.perform(
                get("/reports/userReports")
                        .param("groupBy", "DURATION_REVERSE_ORDER")
                        .principal(userDto)

        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reportsComponent))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userProgress,
                        progressDto.getProgress()))
                .andExpect(model().attribute(AttributeNames.UserViewReports.sumOfDurations,
                        progressDto.getUserActualHoursWorked()))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
                .andReturn();

        Collection<Object> model = result.getModelAndView().getModel().values();

        Object firstDtosArrayList = model.stream().findFirst().get();
        ArrayList<ReportDto> arraydtos = (ArrayList<ReportDto>) firstDtosArrayList;

        assertEquals( arraydtos.get(0), reportDtoToday1);
        assertEquals( arraydtos.get(1), reportDtoToday2);

        verify(mockReportService, times(1))
                .viewUserReportsBetweenDates(userDto.getId(),today, today);
        verify(mockProgressService, times(2))
                .getUserProgressBetweenDates(userDto.getId(), today, today);
        verify(mockCalendarService, times(1))
                .getCalendarInformationBetweenDates(today, today);
    }

    @Test
    public void getUserReportsComponentWithoutFilterAndWithGroupingByDescriptionAsc() throws Exception {
        dtos.clear();

        dtos.add(reportDtoToday1);
        dtos.add(reportDtoToday2);

        LocalDate today = LocalDate.now();

        when(mockReportService.viewUserReportsBetweenDates(
                userDto.getId(), today, today)).thenReturn(dtos);
        when(mockProgressService.getUserProgressBetweenDates(
                userDto.getId(), today, today))
                .thenReturn(progressDto);
        when(mockCalendarService.getCalendarInformationBetweenDates(today, today))
                .thenReturn(calendarDto);

        MvcResult result = this.mockMvc.perform(
                get("/reports/userReports")
                        .param("groupBy", "DESCRIPTION_DIRECT_ORDER")
                        .principal(userDto)

        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reportsComponent))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userProgress,
                        progressDto.getProgress()))
                .andExpect(model().attribute(AttributeNames.UserViewReports.sumOfDurations,
                        progressDto.getUserActualHoursWorked()))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
                .andReturn();

        Collection<Object> model = result.getModelAndView().getModel().values();

        Object firstDtosArrayList = model.stream().findFirst().get();
        ArrayList<ReportDto> arraydtos = (ArrayList<ReportDto>) firstDtosArrayList;

        assertEquals( arraydtos.get(0), reportDtoToday2);
        assertEquals( arraydtos.get(1), reportDtoToday1);

        verify(mockReportService, times(1))
                .viewUserReportsBetweenDates(userDto.getId(),today, today);
        verify(mockProgressService, times(2))
                .getUserProgressBetweenDates(userDto.getId(), today, today);
        verify(mockCalendarService, times(1))
                .getCalendarInformationBetweenDates(today, today);
    }

    @Test
    public void getUserReportsComponentWithoutFilterAndWithGroupingByDescriptionDesc() throws Exception {
        dtos.clear();

        dtos.add(reportDtoToday1);
        dtos.add(reportDtoToday2);

        LocalDate today = LocalDate.now();

        when(mockReportService.viewUserReportsBetweenDates(
                userDto.getId(), today, today)).thenReturn(dtos);
        when(mockProgressService.getUserProgressBetweenDates(
                userDto.getId(), today, today))
                .thenReturn(progressDto);
        when(mockCalendarService.getCalendarInformationBetweenDates(today, today))
                .thenReturn(calendarDto);

        MvcResult result = this.mockMvc.perform(
                get("/reports/userReports")
                        .param("groupBy", "DESCRIPTION_REVERSE_ORDER")
                        .principal(userDto)

        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reportsComponent))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userProgress,
                        progressDto.getProgress()))
                .andExpect(model().attribute(AttributeNames.UserViewReports.sumOfDurations,
                        progressDto.getUserActualHoursWorked()))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
                .andReturn();

        Collection<Object> model = result.getModelAndView().getModel().values();

        Object firstDtosArrayList = model.stream().findFirst().get();
        ArrayList<ReportDto> arraydtos = (ArrayList<ReportDto>) firstDtosArrayList;

        assertEquals( arraydtos.get(0), reportDtoToday1);
        assertEquals( arraydtos.get(1), reportDtoToday2);

        verify(mockReportService, times(1))
                .viewUserReportsBetweenDates(userDto.getId(),today, today);
        verify(mockProgressService, times(2))
                .getUserProgressBetweenDates(userDto.getId(), today, today);
        verify(mockCalendarService, times(1))
                .getCalendarInformationBetweenDates(today, today);
    }

    @Test
    public void getUserReportsComponentWithoutFilterAndWithGroupingByRemoteFirst() throws Exception {
        dtos.clear();

        dtos.add(reportDtoToday1);
        dtos.add(reportDtoToday2);

        LocalDate today = LocalDate.now();

        when(mockReportService.viewUserReportsBetweenDates(
                userDto.getId(), today, today)).thenReturn(dtos);
        when(mockProgressService.getUserProgressBetweenDates(
                userDto.getId(), today, today))
                .thenReturn(progressDto);
        when(mockCalendarService.getCalendarInformationBetweenDates(today, today))
                .thenReturn(calendarDto);

        MvcResult result = this.mockMvc.perform(
                get("/reports/userReports")
                        .param("groupBy", "REMOTE_FIRST")
                        .principal(userDto)

        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reportsComponent))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userProgress,
                        progressDto.getProgress()))
                .andExpect(model().attribute(AttributeNames.UserViewReports.sumOfDurations,
                        progressDto.getUserActualHoursWorked()))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
                .andReturn();

        Collection<Object> model = result.getModelAndView().getModel().values();

        Object firstDtosArrayList = model.stream().findFirst().get();
        ArrayList<ReportDto> arraydtos = (ArrayList<ReportDto>) firstDtosArrayList;

        assertEquals( arraydtos.get(0), reportDtoToday1);
        assertEquals( arraydtos.get(1), reportDtoToday2);

        verify(mockReportService, times(1))
                .viewUserReportsBetweenDates(userDto.getId(),today, today);
        verify(mockProgressService, times(2))
                .getUserProgressBetweenDates(userDto.getId(), today, today);
        verify(mockCalendarService, times(1))
                .getCalendarInformationBetweenDates(today, today);
    }

    @Test
    public void getUserReportsComponentWithoutFilterAndWithGroupingByRemoteLast() throws Exception {
        dtos.clear();

        dtos.add(reportDtoToday1);
        dtos.add(reportDtoToday2);

        LocalDate today = LocalDate.now();

        when(mockReportService.viewUserReportsBetweenDates(
                userDto.getId(), today, today)).thenReturn(dtos);
        when(mockProgressService.getUserProgressBetweenDates(
                userDto.getId(), today, today))
                .thenReturn(progressDto);
        when(mockCalendarService.getCalendarInformationBetweenDates(today, today))
                .thenReturn(calendarDto);

        MvcResult result = this.mockMvc.perform(
                get("/reports/userReports")
                        .param("groupBy", "REMOTE_LAST")
                        .principal(userDto)

        )
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.REPORTS.reportsComponent))
                .andExpect(model().attribute(AttributeNames.UserViewReports.userProgress,
                        progressDto.getProgress()))
                .andExpect(model().attribute(AttributeNames.UserViewReports.sumOfDurations,
                        progressDto.getUserActualHoursWorked()))
                .andExpect(model().attribute(AttributeNames.ProgressView.calendar, calendarDto))
                .andReturn();

        Collection<Object> model = result.getModelAndView().getModel().values();

        Object firstDtosArrayList = model.stream().findFirst().get();
        ArrayList<ReportDto> arraydtos = (ArrayList<ReportDto>) firstDtosArrayList;

        assertEquals( arraydtos.get(0), reportDtoToday2);
        assertEquals( arraydtos.get(1), reportDtoToday1);

        verify(mockReportService, times(1))
                .viewUserReportsBetweenDates(userDto.getId(),today, today);
        verify(mockProgressService, times(2))
                .getUserProgressBetweenDates(userDto.getId(), today, today);
        verify(mockCalendarService, times(1))
                .getCalendarInformationBetweenDates(today, today);
    }

    @Test
    public void createReport() throws Exception {
        this.mockMvc.perform(
                post("/reports/add")
                        .principal(userDto)
                        .param("date", reportDto.getDate().toString())
                        .param("duration", Double.toString(reportDto.getDuration()))
                        .param("description", reportDto.getDescription())
                        .param("remote", Boolean.toString(reportDto.isRemote()))

       )
                .andExpect(status().isOk())
        ;

        verify(mockReportService, only())
                .createReport(
                        reportDto.getDate(),
                        reportDto.getDuration(),
                        reportDto.getDescription(),
                        reportDto.getUserId(),
                        reportDto.isRemote()
               );
    }

    @Test
    public void createReportInvalidDate() throws Exception {
        doThrow(new DateTimeParseException(ErrorKeys.DateParseMessage, reportDto.getDate().toString(), 0))
                .when(mockReportService).
                createReport(reportDto.getDate(), reportDto.getDuration(),
                        reportDto.getDescription(), userDto.getId(), reportDto.isRemote());

        this.mockMvc.perform(
                post("/reports/add")
                        .principal(userDto)
                        .param("date", reportDto.getDate().toString())
                        .param("duration", Double.toString(reportDto.getDuration()))
                        .param("description", reportDto.getDescription())
                        .param("remote", Boolean.toString(reportDto.isRemote()))

        )
                .andExpect(view().name("error"))
                .andExpect( (result) ->
                        result.getResponse().getContentAsString().contains(ErrorKeys.DateParseMessage)
                );
    }

    @Test
    public void editReport() throws Exception {
        this.mockMvc.perform(
                post("/reports/edit")
                        .param("reportId", reportId)
                        .param("date", reportDto.getDate().toString())
                        .param("duration", Double.toString(reportDto.getDuration()))
                        .param("description", reportDto.getDescription())
                        .param("remote", Boolean.toString(reportDto.isRemote()))
       )
                .andExpect(status().isOk())
        ;

        verify(mockReportService, only())
                .editReport(
                        reportId,
                        reportDto.getDate(),
                        reportDto.getDuration(),
                        reportDto.getDescription(),
                        reportDto.isRemote()
               );
    }

    @Test
    public void editReportInvalidDate() throws Exception {
        doThrow(new DateTimeParseException(ErrorKeys.DateParseMessage, reportDto.getDate().toString(), 0))
                .when(mockReportService).
                editReport(reportDto.getId(), reportDto.getDate(), reportDto.getDuration(),
                        reportDto.getDescription(), reportDto.isRemote());

        this.mockMvc.perform(
                post("/reports/edit")
                        .param("reportId", reportId)
                        .param("date", reportDto.getDate().toString())
                        .param("duration", Double.toString(reportDto.getDuration()))
                        .param("description", reportDto.getDescription())
                        .param("remote", Boolean.toString(reportDto.isRemote()))

        )
                .andExpect(view().name("error"))
                .andExpect( (result) ->
                        result.getResponse().getContentAsString().contains(ErrorKeys.DateParseMessage)
                );
    }

    @Test
    public void deleteReport() throws Exception {
        this.mockMvc.perform(
                post("/reports/delete")
                        .param("reportId", reportId)
       )
                .andExpect(status().isOk()
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
                .andExpect( (result) ->
                        result.getResponse().getContentAsString().contains(ErrorKeys.EntityNotFoundMessage)
                );
    }
}
