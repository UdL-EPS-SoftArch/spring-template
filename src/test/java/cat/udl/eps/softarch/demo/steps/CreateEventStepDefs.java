package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Category;
import cat.udl.eps.softarch.demo.domain.Subcategory;
import cat.udl.eps.softarch.demo.repository.CategoryRepository;
import cat.udl.eps.softarch.demo.repository.SubcategoryRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class CreateEventStepDefs {
    private final StepDefs stepDefs;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    // URI of the event returned by the last successful creation, read back by the Then steps
    private String createdEventUri;

    // Valid event payload, used by the "without <field>" scenarios
    private static final Map<String, String> VALID_EVENT = Map.of(
            "title", "Welcome picnic",
            "description", "Meet other Erasmus students",
            "address", "City park",
            "dateTime", "2030-11-10T16:00:00",
            "maxParticipants", "20",
            "estimatedCost", "5.00",
            "subcategory", "Meetups");

    public CreateEventStepDefs(StepDefs stepDefs, CategoryRepository categoryRepository,
                               SubcategoryRepository subcategoryRepository) {
        this.stepDefs = stepDefs;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @Given("a category named {string} exists")
    public void aCategoryNamedExists(String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
    }

    @Given("a subcategory named {string} belongs to category {string}")
    public void aSubcategoryNamedBelongsToCategory(String name, String categoryName) {
        Category category = categoryRepository.findByName(categoryName).stream().findFirst().orElseThrow();
        Subcategory subcategory = new Subcategory();
        subcategory.setName(name);
        subcategory.setCategory(category);
        subcategoryRepository.save(subcategory);
    }

    @When("I create an event with:")
    public void iCreateAnEventWith(DataTable table) throws Throwable {
        postEvent(table.asMap(String.class, String.class));
    }

    @When("I create an event without {string}")
    public void iCreateAnEventWithout(String field) throws Throwable {
        Map<String, String> fields = new HashMap<>(VALID_EVENT);
        fields.remove(field);
        postEvent(fields);
    }

    @And("the created event has title {string}")
    public void theCreatedEventHasTitle(String title) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get(createdEventUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.title", is(title)));
    }

    @And("the created event is owned by {string}")
    public void theCreatedEventIsOwnedBy(String username) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get(createdEventUri + "/ownedBy")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.username", is(username)));
    }

    private void postEvent(Map<String, String> fields) throws Throwable {
        Map<String, Object> body = new HashMap<>(fields);
        if (fields.containsKey("subcategory")) {
            // Scenarios refer to subcategories by name; the API expects the resource URI
            Subcategory subcategory = subcategoryRepository.findByName(fields.get("subcategory")).stream().findFirst().orElseThrow();
            body.put("subcategory", subcategory.getUri());
        }

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(body))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());

        createdEventUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }
}
