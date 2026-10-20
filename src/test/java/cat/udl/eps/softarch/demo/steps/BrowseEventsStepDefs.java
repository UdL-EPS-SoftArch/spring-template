package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.domain.Subcategory;
import cat.udl.eps.softarch.demo.repository.EventRepository;
import cat.udl.eps.softarch.demo.repository.SubcategoryRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BrowseEventsStepDefs {
    private static final String OWNER_USERNAME = "demo";
    private static final String OWNER_PASSWORD = "password";
    private static final String DEFAULT_ADDRESS = "City park";

    // Ids are generated from 1, so this URI never matches an existing event
    private static final String MISSING_EVENT_URI = "/events/0";

    private final StepDefs stepDefs;
    private final EventRepository eventRepository;
    private final SubcategoryRepository subcategoryRepository;

    public BrowseEventsStepDefs(StepDefs stepDefs, EventRepository eventRepository,
                                SubcategoryRepository subcategoryRepository) {
        this.stepDefs = stepDefs;
        this.eventRepository = eventRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @Given("an event titled {string} exists in subcategory {string}")
    public void anEventTitledExistsInSubcategory(String title, String subcategoryName) throws Exception {
        createEvent(title, subcategoryName, DEFAULT_ADDRESS);
    }

    @Given("an event titled {string} exists in subcategory {string} at address {string}")
    public void anEventTitledExistsInSubcategoryAtAddress(String title, String subcategoryName, String address)
            throws Exception {
        createEvent(title, subcategoryName, address);
    }

    @Given("no events exist in category {string}")
    public void noEventsExistInCategory(String categoryName) {
        eventsInCategory(categoryName).forEach(eventRepository::delete);
    }

    @When("I view the public events")
    public void iViewThePublicEvents() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/events")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I filter public events by category {string}")
    public void iFilterPublicEventsByCategory(String categoryName) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/events/search/findBySubcategory_Category_NameIgnoreCase?name={name}", categoryName)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I view the event titled {string}")
    public void iViewTheEventTitled(String title) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get(resolveEventUri(title))
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @Then("the public events include {string}")
    public void thePublicEventsInclude(String title) throws Exception {
        stepDefs.result.andExpect(jsonPath("$..title", hasItem(title)));
    }

    @Then("the public events do not include {string}")
    public void thePublicEventsDoNotInclude(String title) throws Exception {
        stepDefs.result.andExpect(jsonPath("$..title", not(hasItem(title))));
    }

    @Then("the public events list is empty")
    public void thePublicEventsListIsEmpty() throws Exception {
        stepDefs.result.andExpect(jsonPath("$..title", empty()));
    }

    @Then("the event has address {string}")
    public void theEventHasAddress(String address) throws Exception {
        stepDefs.result.andExpect(jsonPath("$.address", is(address)));
    }

    private void createEvent(String title, String subcategoryName, String address) throws Exception {
        // Earlier scenarios leave events behind, so clear this title to keep title lookups unambiguous
        eventsTitled(title).forEach(eventRepository::delete);

        Subcategory subcategory = subcategoryRepository.findByName(subcategoryName).stream().findFirst().orElseThrow();
        Map<String, Object> body = new HashMap<>();
        body.put("title", title);
        body.put("description", "Meet other Erasmus students");
        body.put("address", address);
        body.put("dateTime", "2030-11-10T16:00:00");
        body.put("maxParticipants", 20);
        body.put("estimatedCost", "5.00");
        body.put("subcategory", subcategory.getUri());

        stepDefs.mockMvc.perform(
                post("/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(body))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(httpBasic(OWNER_USERNAME, OWNER_PASSWORD)))
            .andExpect(status().isCreated());
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

    private List<Event> eventsInCategory(String categoryName) {
        return StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .filter(event -> categoryName.equals(event.getSubcategory().getCategory().getName()))
                .toList();
    }
}
