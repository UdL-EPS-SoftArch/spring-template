package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SuspendUserStepDefs {
    private static final String ADMIN_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";

    private final StepDefs stepDefs;
    private final UserRepository userRepository;

    public SuspendUserStepDefs(StepDefs stepDefs, UserRepository userRepository) {
        this.stepDefs = stepDefs;
        this.userRepository = userRepository;
    }

    @Given("the admin has suspended user {string}")
    public void theAdminHasSuspendedUser(String username) throws Exception {
        stepDefs.mockMvc.perform(
                put("/users/{username}/suspension", username)
                    .with(httpBasic(ADMIN_USERNAME, DEFAULT_PASSWORD)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @When("I suspend the user {string}")
    public void iSuspendTheUser(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                put("/users/{username}/suspension", username)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I reactivate the user {string}")
    public void iReactivateTheUser(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/users/{username}/suspension", username)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @And("the user {string} is suspended")
    public void theUserIsSuspended(String username) {
        assertEquals(true, userRepository.findById(username).orElseThrow().isSuspended());
    }

    @And("the user {string} is not suspended")
    public void theUserIsNotSuspended(String username) {
        assertEquals(false, userRepository.findById(username).orElseThrow().isSuspended());
    }
}
