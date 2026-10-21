package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Report;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.ReportRepository;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RepositoryEventHandler
public class ReportEventHandler {

    private final ReportRepository reportRepository;

    public ReportEventHandler(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @HandleBeforeCreate
    public void handleBeforeCreate(Report report) {
        if ((report.getEvent() == null) == (report.getComment() == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A report must target exactly one event or comment");
        }

        User reporter = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        report.setReporter(reporter);

        boolean alreadyReported = report.getEvent() != null
                ? reportRepository.existsByReporterAndEvent(reporter, report.getEvent())
                : reportRepository.existsByReporterAndComment(reporter, report.getComment());
        if (alreadyReported) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already reported this content");
        }
    }
}
