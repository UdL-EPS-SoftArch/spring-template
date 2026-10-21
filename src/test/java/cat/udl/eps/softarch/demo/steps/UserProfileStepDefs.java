package cat.udl.eps.softarch.demo.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserProfileStepDefs {
    private final StepDefs stepDefs;

    public UserProfileStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @When("I view the profile of {string}")
    public void iViewTheProfileOf(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/users/{username}", username)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I update my profile with:")
    public void iUpdateMyProfileWith(DataTable table) throws Exception {
        updateProfile(AuthenticationStepDefs.currentUsername, table);
    }

    @When("I update the profile of {string} with:")
    public void iUpdateTheProfileOfWith(String username, DataTable table) throws Exception {
        updateProfile(username, table);
    }

    @Then("the profile has username {string}")
    public void theProfileHasUsername(String username) throws Exception {
        stepDefs.result.andExpect(jsonPath("$.username", is(username)));
    }

    @And("the profile has email {string}")
    public void theProfileHasEmail(String email) throws Exception {
        stepDefs.result.andExpect(jsonPath("$.email", is(email)));
    }

    @And("the profile has full name {string}")
    public void theProfileHasFullName(String fullName) throws Exception {
        stepDefs.result.andExpect(jsonPath("$.fullName", is(fullName)));
    }

    @And("the profile does not expose the password")
    public void theProfileDoesNotExposeThePassword() throws Exception {
        stepDefs.result.andExpect(jsonPath("$.password").doesNotExist());
    }

    private void updateProfile(String username, DataTable table) throws Exception {
        // PATCH so that fields missing from the table keep their stored values
        stepDefs.result = stepDefs.mockMvc.perform(
                patch("/users/{username}", username)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(table.asMap(String.class, String.class)))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }
}
