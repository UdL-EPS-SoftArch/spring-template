package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Comment;
import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.repository.CommentRepository;
import cat.udl.eps.softarch.demo.repository.EventRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventCommentsStepDefs {
    private static final String DEFAULT_PASSWORD = "password";

    // Ids are generated from 1, so this URI never matches an existing event
    private static final String MISSING_EVENT_URI = "/events/0";

    private final StepDefs stepDefs;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    // URI of the comment returned by the last successful creation, read back by the Then steps
    private String createdCommentUri;

    public EventCommentsStepDefs(StepDefs stepDefs, EventRepository eventRepository,
                                 CommentRepository commentRepository) {
        this.stepDefs = stepDefs;
        this.eventRepository = eventRepository;
        this.commentRepository = commentRepository;
    }

    @Given("a comment with content {string} exists on the event titled {string} and is authored by {string}")
    public void aCommentExistsOnTheEventAndIsAuthoredBy(String content, String title, String author) throws Exception {
        ResultActions created = postComment(content, eventUri(title), httpBasic(author, DEFAULT_PASSWORD));
        created.andExpect(status().isCreated());
        createdCommentUri = created.andReturn().getResponse().getHeader("Location");
    }

    @When("I post a comment with content {string} on the event titled {string}")
    public void iPostACommentWithContentOnTheEventTitled(String content, String title) throws Exception {
        stepDefs.result = postComment(content, eventUri(title), AuthenticationStepDefs.authenticate());
        createdCommentUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }

    @When("I view comments for the event titled {string}")
    public void iViewCommentsForTheEventTitled(String title) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get(eventUri(title) + "/comments")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I delete the comment with content {string}")
    public void iDeleteTheCommentWithContent(String content) throws Exception {
        Comment comment = StreamSupport.stream(commentRepository.findAll().spliterator(), false)
                .filter(candidate -> content.equals(candidate.getContent()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No comment with content \"" + content + "\" exists"));
        stepDefs.result = stepDefs.mockMvc.perform(
                delete(comment.getUri())
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @Then("the comments list is empty")
    public void theCommentsListIsEmpty() throws Exception {
        stepDefs.result.andExpect(jsonPath("$..content", empty()));
    }

    @And("the comment with content {string} still exists")
    public void theCommentStillExists(String content) {
        org.junit.jupiter.api.Assertions.assertTrue(
                StreamSupport.stream(commentRepository.findAll().spliterator(), false)
                        .anyMatch(comment -> content.equals(comment.getContent())));
    }

    @Then("the comments include {string}")
    public void theCommentsInclude(String content) throws Exception {
        stepDefs.result.andExpect(jsonPath("$..content", hasItem(content)));
    }

    @And("that comment is authored by {string}")
    public void thatCommentIsAuthoredBy(String username) throws Exception {
        assertCommentAuthor(username);
    }

    @And("the created comment has content {string}")
    public void theCreatedCommentHasContent(String content) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get(createdCommentUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.content", is(content)));
    }

    @And("the created comment is authored by {string}")
    public void theCreatedCommentIsAuthoredBy(String username) throws Exception {
        assertCommentAuthor(username);
    }

    @And("the created comment belongs to the event titled {string}")
    public void theCreatedCommentBelongsToTheEventTitled(String title) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get(createdCommentUri + "/event")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.title", is(title)));
    }

    private void assertCommentAuthor(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get(createdCommentUri + "/author")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.username", is(username)));
    }

    private ResultActions postComment(String content, String eventUri, RequestPostProcessor authentication)
            throws Exception {
        Map<String, String> body = Map.of("content", content, "event", eventUri);
        return stepDefs.mockMvc.perform(
                post("/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(body))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(authentication))
            .andDo(print());
    }

    private String eventUri(String title) {
        List<Event> events = StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .filter(event -> title.equals(event.getTitle()))
                .toList();
        return events.isEmpty() ? MISSING_EVENT_URI : events.get(0).getUri();
    }
}
