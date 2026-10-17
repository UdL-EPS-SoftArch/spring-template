package cat.udl.eps.softarch.demo.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import tools.jackson.databind.JsonNode;
import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.domain.Subcategory;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.EventRepository;
import cat.udl.eps.softarch.demo.repository.SubcategoryRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JoinLeaveEventsStepDefs {
    private static final String DEFAULT_PASSWORD = "password";
    private static final String DEFAULT_SUBCATEGORY = "Meetups";

    private final StepDefs stepDefs;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final SubcategoryRepository subcategoryRepository;

    public JoinLeaveEventsStepDefs(StepDefs stepDefs, EventRepository eventRepository,
                                   UserRepository userRepository, SubcategoryRepository subcategoryRepository) {
        this.stepDefs = stepDefs;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @Given("an event titled {string} with maximum {int} participant exists and is owned by {string}")
    public void anEventWithMaximumParticipantsExistsAndIsOwnedBy(String title, int maximum, String ownerUsername) {
        eventsTitled(title).forEach(eventRepository::delete);

        User owner = userRepository.findById(ownerUsername).orElseThrow();
        Event event = new Event();
        event.setTitle(title);
        event.setDescription("Meet other Erasmus students");
        event.setAddress("City park");
        event.setDateTime(LocalDateTime.of(2030, 11, 10, 16, 0));
        event.setMaxParticipants(maximum);
        event.setEstimatedCost(new BigDecimal("5.00"));
        event.setSubcategory(subcategoryNamed(DEFAULT_SUBCATEGORY));
        event.setCreator(owner);
        eventRepository.save(event);
    }

    @Given("the event titled {string} has available places")
    public void theEventHasAvailablePlaces(String title) {
        Event event = eventTitled(title);
        if (event.getMaxParticipants() <= 1) {
            event.setMaxParticipants(20);
            eventRepository.save(event);
        }
    }

    @Given("^\"([^\"]+)\" is (?:already )?registered for the event titled \"([^\"]+)\"$")
    public void aUserIsRegisteredForTheEvent(String username, String title) throws Exception {
        if (AuthenticationStepDefs.currentUsername == null || AuthenticationStepDefs.currentUsername.isEmpty()) {
            stepDefs.result = stepDefs.mockMvc.perform(
                    post(participantsUri(title))
                        .with(httpBasic(username, DEFAULT_PASSWORD)))
                .andDo(print());
        } else {
            assertRegistrationCount(username, title, 1);
        }
    }

    @Given("{string} is not registered for the event titled {string}")
    public void aUserIsNotRegisteredForTheEvent(String username, String title) throws Exception {
        assertRegistrationCount(username, title, 0);
    }

    @When("^I (join|leave) the event titled \"([^\"]+)\"$")
    public void iJoinOrLeaveTheEvent(String action, String title) throws Exception {
        String uri = participantsUri(title);
        if ("join".equals(action)) {
            stepDefs.result = stepDefs.mockMvc.perform(
                    post(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
        } else if ("leave".equals(action)) {
            stepDefs.result = stepDefs.mockMvc.perform(
                    delete(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
        } else {
            throw new IllegalArgumentException("Unsupported event action: " + action);
        }
    }

    @Then("{string} is registered only once for the event titled {string}")
    public void aUserIsRegisteredOnlyOnceForTheEvent(String username, String title) throws Exception {
        assertRegistrationCount(username, title, 1);
    }

    private void assertRegistrationCount(String username, String title, int expectedCount) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get(participantsUri(title))
                    .accept(MediaType.APPLICATION_JSON)
                    .with(httpBasic(username, DEFAULT_PASSWORD)))
            .andDo(print())
            .andExpect(status().isOk());

        JsonNode registrations = stepDefs.mapper.readTree(
                stepDefs.result.andReturn().getResponse().getContentAsString());
        int count = countUserReferences(registrations, username);
        if (expectedCount == 0) {
            assertEquals(0, count, username + " should not be registered for " + title);
        } else {
            assertTrue(count > 0, username + " should be registered for " + title);
            assertEquals(expectedCount, count, username + " should be registered only once for " + title);
        }
    }

    private int countUserReferences(JsonNode node, String username) {
        if (node == null || node.isNull()) {
            return 0;
        }
        if (node.isString()) {
            return isUserReference(node.stringValue(), username) ? 1 : 0;
        }
        if (node.isArray()) {
            int count = 0;
            for (JsonNode child : node) {
                count += countUserReferences(child, username);
            }
            return count;
        }
        if (node.isObject()) {
            if (isUserObject(node, username)) {
                return 1;
            }
            int count = 0;
            for (JsonNode child : node) {
                count += countUserReferences(child, username);
            }
            return count;
        }
        return 0;
    }

    private boolean isUserObject(JsonNode node, String username) {
        if (username.equals(textValue(node.get("username"))) || username.equals(textValue(node.get("id")))
                || isUserReference(textValue(node.get("uri")), username)) {
            return true;
        }
        JsonNode selfHref = node.path("_links").path("self").get("href");
        return selfHref != null && isUserReference(selfHref.asString(), username);
    }

    private boolean isUserReference(String value, String username) {
        return value != null && (value.equals(username) || value.endsWith("/users/" + username));
    }

    private String textValue(JsonNode node) {
        return node != null && node.isString() ? node.stringValue() : null;
    }

    private String participantsUri(String title) {
        return eventTitled(title).getUri() + "/participants";
    }

    private Event eventTitled(String title) {
        return eventsTitled(title).stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No event titled \"" + title + "\" exists"));
    }

    private List<Event> eventsTitled(String title) {
        return StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .filter(event -> title.equals(event.getTitle()))
                .toList();
    }

    private Subcategory subcategoryNamed(String name) {
        return subcategoryRepository.findByName(name).stream().findFirst().orElseThrow();
    }
}
