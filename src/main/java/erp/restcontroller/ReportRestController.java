package erp.restcontroller;

import erp.dto.ReportDto;
import erp.service.IReportService;
import erp.utils.DateParser;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/reports")
public class ReportRestController {

    @Inject
    private IReportService reportService;

    private final int DATE_LENGTH_MONTH_FORMAT = 7; //E.g "YYYY-MM"
    private final int DATE_LENGTH_FULL_FORMAT = 10; //E.g "YYYY-MM-DD"

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public List<ReportDto> findAll(@PathVariable String userId, @RequestParam Optional<String> filter) {
        LocalDate begin = null;
        LocalDate end = null;

        if (filter.isPresent()) {
            LocalDate localDate = null;
            String filterDate = filter.get();

            //Check if filter string came in month format or full format
            if (filterDate.length() == DATE_LENGTH_MONTH_FORMAT) {
                localDate = DateParser.parseMonthDate(filterDate);
                begin = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
                end = LocalDate.of(localDate.getYear(), localDate.getMonth(), begin.lengthOfMonth());
            }
            else if (filterDate.length() == DATE_LENGTH_FULL_FORMAT) {
                localDate = DateParser.parseDate(filterDate);
                begin = localDate;
                end = localDate;
            }
        }
        else
            begin = end = LocalDate.now();
        return reportService.viewUserReportsBetweenDates(userId, begin, end);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    public ReportDto add(@PathVariable String userId, @RequestBody ReportDto report) {
        String reportId = reportService.createReport(
                report.getDate(), report.getDuration(),
                report.getDescription(), userId, report.isRemote());
        return reportService.findReport(reportId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ReportDto update(@PathVariable String id, @RequestBody ReportDto report) {
        reportService.editReport(
                id, report.getDate(), report.getDuration(),
                report.getDescription(), report.isRemote());
        return reportService.findReport(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void remove(@PathVariable String id) {
        reportService.removeReport(id);
    }
}
