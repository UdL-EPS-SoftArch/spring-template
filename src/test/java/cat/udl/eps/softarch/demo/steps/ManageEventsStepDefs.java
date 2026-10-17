package cat.udl.eps.softarch.demo.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.domain.Subcategory;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.EventRepository;
import cat.udl.eps.softarch.demo.repository.SubcategoryRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;

import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ManageEventsStepDefs {
    private static final String ADMIN_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String DEFAULT_SUBCATEGORY = "Meetups";

    // Ids are generated from 1, so this URI never matches an existing event
    private static final String MISSING_EVENT_URI = "/events/0";

    // Valid payload for the "without <field>" scenarios; subcategory and owner are added as URIs at request time
    private static final Map<String, String> VALID_EVENT = Map.of(
            "title", "Welcome picnic",
            "description", "Meet other Erasmus students",
            "address", "City park",
            "dateTime", "2030-11-10T16:00:00",
            "maxParticipants", "20",
            "estimatedCost", "5.00");

    private final StepDefs stepDefs;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final SubcategoryRepository subcategoryRepository;

    // URIs of the events created in this scenario, keyed by title, read back by the not-found step
    private final Map<String, String> eventUrisByTitle = new HashMap<>();

    public ManageEventsStepDefs(StepDefs stepDefs, UserRepository userRepository,
                                EventRepository eventRepository, SubcategoryRepository subcategoryRepository) {
        this.stepDefs = stepDefs;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @Given("an event titled {string} exists and is owned by {string}")
    public void anEventTitledExistsAndIsOwnedBy(String title, String ownerUsername) {
        // Earlier scenarios leave events behind, so clear this title to keep title lookups unambiguous
        eventsTitled(title).forEach(eventRepository::delete);

        // Some scenarios name an owner without registering them first
        User owner = userRepository.findById(ownerUsername).orElseGet(() -> registerUser(ownerUsername, false));
        Event event = new Event();
        event.setTitle(title);
        event.setDescription("Meet other Erasmus students");
        event.setAddress("City park");
        event.setDateTime(LocalDateTime.of(2030, 11, 10, 16, 0));
        event.setMaxParticipants(20);
        event.setEstimatedCost(new BigDecimal("5.00"));
        event.setSubcategory(subcategoryNamed(DEFAULT_SUBCATEGORY));
        event.setOwnedBy(owner);

        eventUrisByTitle.put(title, eventRepository.save(event).getUri());
    }

    @Given("an admin user exists")
    public void anAdminUserExists() {
        if (!userRepository.existsById(ADMIN_USERNAME)) {
            registerUser(ADMIN_USERNAME, true);
        }
    }

    @And("I login as an admin")
    public void iLoginAsAnAdmin() {
        AuthenticationStepDefs.currentUsername = ADMIN_USERNAME;
        AuthenticationStepDefs.currentPassword = DEFAULT_PASSWORD;
    }

    @When("I update the event titled {string} with:")
    public void iUpdateTheEventTitledWith(String title, DataTable table) throws Throwable {
        Map<String, Object> body = new HashMap<>(table.asMap(String.class, String.class));
        if (body.containsKey("subcategory")) {
            body.put("subcategory", subcategoryNamed((String) body.get("subcategory")).getUri());
        }

        // PATCH so that fields missing from the table keep their stored values
        stepDefs.result = stepDefs.mockMvc.perform(
                patch(resolveEventUri(title))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(body))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I update the event titled {string} without {string}")
    public void iUpdateTheEventTitledWithout(String title, String field) throws Throwable {
        Event event = eventsTitled(title).stream().findFirst().orElseThrow();

        // PUT replaces the whole resource, so a removed field is missing rather than kept from the stored event
        Map<String, Object> body = new HashMap<>(VALID_EVENT);
        body.put("subcategory", subcategoryNamed(DEFAULT_SUBCATEGORY).getUri());
        body.put("ownedBy", event.getOwnedBy().getUri());
        body.remove(field);

        stepDefs.result = stepDefs.mockMvc.perform(
                put(event.getUri())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(body))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I delete the event titled {string}")
    public void iDeleteTheEventTitled(String title) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                delete(resolveEventUri(title))
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @Then("the event titled {string} is owned by {string}")
    public void theEventTitledIsOwnedBy(String title, String username) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get(resolveEventUri(title) + "/ownedBy")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.username", is(username)));
    }

    @Then("the event titled {string} does not exist")
    public void theEventTitledDoesNotExist(String title) throws Throwable {
        String uri = eventUrisByTitle.get(title);
        if (uri == null) {
            throw new IllegalStateException("No event titled \"" + title + "\" was created in this scenario");
        }

        stepDefs.result = stepDefs.mockMvc.perform(
                get(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    private String resolveEventUri(String title) {
        List<Event> events = eventsTitled(title);
        return events.isEmpty() ? MISSING_EVENT_URI : events.get(0).getUri();
    }

    private List<Event> eventsTitled(String title) {
        // The unfiltered CrudRepository.findAll() is used so that lookups do not depend on a logged-in user
        return StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .filter(event -> title.equals(event.getTitle()))
                .toList();
    }

    private Subcategory subcategoryNamed(String name) {
        return subcategoryRepository.findByName(name).stream().findFirst().orElseThrow();
    }

    private User registerUser(String username, boolean admin) {
        User user = new User();
        user.setId(username);
        user.setEmail(username + "@sample.app");
        user.setPassword(DEFAULT_PASSWORD);
        user.setAdmin(admin);
        user.encodePassword();
        return userRepository.save(user);
    }
}
