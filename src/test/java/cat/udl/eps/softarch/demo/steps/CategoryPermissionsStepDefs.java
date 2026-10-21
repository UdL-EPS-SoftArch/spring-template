package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.repository.CategoryRepository;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class CategoryPermissionsStepDefs {
    private final StepDefs stepDefs;
    private final CategoryRepository categoryRepository;

    public CategoryPermissionsStepDefs(StepDefs stepDefs, CategoryRepository categoryRepository) {
        this.stepDefs = stepDefs;
        this.categoryRepository = categoryRepository;
    }

    @When("I create a category named {string}")
    public void iCreateACategoryNamed(String name) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(Map.of("name", name)))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I delete the category named {string}")
    public void iDeleteTheCategoryNamed(String name) throws Exception {
        Long categoryId = categoryRepository.findByName(name).orElseThrow().getId();
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/categories/{id}", categoryId)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @Then("the category named {string} has not been created")
    public void theCategoryNamedHasNotBeenCreated(String name) {
        assertThat(categoryRepository.findByName(name).isEmpty(), is(true));
    }

    @Then("the category named {string} has been created")
    public void theCategoryNamedHasBeenCreated(String name) {
        assertThat(categoryRepository.findByName(name).isPresent(), is(true));
    }

    @Then("the category named {string} still exists")
    public void theCategoryNamedStillExists(String name) {
        assertThat(categoryRepository.findByName(name).isPresent(), is(true));
    }

    @Then("the category named {string} no longer exists")
    public void theCategoryNamedNoLongerExists(String name) {
        assertThat(categoryRepository.findByName(name).isEmpty(), is(true));
    }
}
