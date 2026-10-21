package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Comment;
import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.CommentRepository;
import cat.udl.eps.softarch.demo.repository.EventRepository;
import cat.udl.eps.softarch.demo.repository.ReportRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import tools.jackson.databind.JsonNode;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReportContentStepDefs {
    private static final String DEFAULT_PASSWORD = "password";

    private final StepDefs stepDefs;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    public ReportContentStepDefs(StepDefs stepDefs, EventRepository eventRepository,
                                 CommentRepository commentRepository, UserRepository userRepository,
                                 ReportRepository reportRepository) {
        this.stepDefs = stepDefs;
        this.eventRepository = eventRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
    }

    @Given("the event titled {string} has been reported by {string}")
    public void theEventHasBeenReportedBy(String title, String username) throws Exception {
        ensureUser(username);
        postReport(Map.of("event", eventUri(title)), httpBasic(username, DEFAULT_PASSWORD))
            .andExpect(status().isCreated());
    }

    @Given("I have already reported the event titled {string}")
    public void iHaveAlreadyReportedTheEvent(String title) throws Exception {
        postReport(Map.of("event", eventUri(title)), AuthenticationStepDefs.authenticate())
            .andExpect(status().isCreated());
    }

    @When("I report the event titled {string}")
    public void iReportTheEvent(String title) throws Exception {
        stepDefs.result = postReport(Map.of("event", eventUri(title)), AuthenticationStepDefs.authenticate());
    }

    @When("I report the comment with content {string}")
    public void iReportTheComment(String content) throws Exception {
        Comment comment = StreamSupport.stream(commentRepository.findAll().spliterator(), false)
                .filter(candidate -> content.equals(candidate.getContent()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No comment with content \"" + content + "\" exists"));
        stepDefs.result = postReport(Map.of("comment", comment.getUri()), AuthenticationStepDefs.authenticate());
    }

    @Then("I see a confirmation that my report was submitted")
    public void iSeeAConfirmation() throws Exception {
        stepDefs.result.andExpect(status().isCreated());
    }

    @Then("I see a warning that I have already reported this content")
    public void iSeeAWarning() throws Exception {
        stepDefs.result.andExpect(status().isConflict());
    }

    @And("only one report from {string} exists for the event titled {string}")
    public void onlyOneReportExists(String username, String title) {
        assertEquals(1, reportRepository.countByReporter_IdAndEvent_Id(username, eventTitled(title).getId()));
    }

    @Then("I am redirected to the login or registration page")
    public void iAmRedirectedToTheLoginOrRegistrationPage() throws Exception {
        AuthenticationStepDefs.assertAuthenticationChallenge(stepDefs.result);
    }

    @And("I see a message asking me to log in or register")
    public void iSeeAMessageAskingMeToLogInOrRegister() throws Exception {
        AuthenticationStepDefs.assertAuthenticationChallenge(stepDefs.result);
    }

    @When("I list the flagged content")
    public void iListTheFlaggedContent() throws Exception {
        stepDefs.result = listReports();
    }

    @And("the flagged content includes the event titled {string}")
    public void theFlaggedContentIncludesTheEvent(String title) throws Exception {
        assertTrue(flaggedEventTitles().contains(title), "Flagged content should include " + title);
    }

    @And("the flagged content does not include the event titled {string}")
    public void theFlaggedContentDoesNotIncludeTheEvent(String title) throws Exception {
        assertFalse(flaggedEventTitles().contains(title), "Flagged content should not include " + title);
    }

    // Reports expose their target as an association link, so each event is followed to read its title
    private List<String> flaggedEventTitles() throws Exception {
        String body = listReports().andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<String> titles = new ArrayList<>();
        for (JsonNode report : stepDefs.mapper.readTree(body).path("_embedded").path("reports")) {
            JsonNode eventLink = report.path("_links").path("event").path("href");
            if (eventLink.isString()) {
                String eventBody = stepDefs.mockMvc.perform(
                        get(URI.create(eventLink.stringValue()).getPath())
                            .accept(MediaType.APPLICATION_JSON)
                            .with(AuthenticationStepDefs.authenticate()))
                    .andReturn().getResponse().getContentAsString();
                titles.add(stepDefs.mapper.readTree(eventBody).path("title").asString());
            }
        }
        return titles;
    }

    private ResultActions listReports() throws Exception {
        return stepDefs.mockMvc.perform(
                get("/reports")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    private ResultActions postReport(Map<String, String> body, RequestPostProcessor authentication) throws Exception {
        return stepDefs.mockMvc.perform(
                post("/reports")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(body))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(authentication))
            .andDo(print());
    }

    private void ensureUser(String username) {
        if (!userRepository.existsById(username)) {
            User user = new User();
            user.setId(username);
            user.setFullName(username);
            user.setEmail(username + "@sample.app");
            user.setPassword(DEFAULT_PASSWORD);
            user.encodePassword();
            userRepository.save(user);
        }
    }

    private String eventUri(String title) {
        return eventTitled(title).getUri();
    }

    private Event eventTitled(String title) {
        return StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .filter(event -> title.equals(event.getTitle()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No event titled \"" + title + "\" exists"));
    }
}
