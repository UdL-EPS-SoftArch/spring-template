package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginLogoutStepDefs {
    private static final String ADMIN_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";

    private final StepDefs stepDefs;
    private final AuthenticationStepDefs authentication;
    private final UserRepository userRepository;

    private boolean adminPasswordChanged;

    public LoginLogoutStepDefs(StepDefs stepDefs, AuthenticationStepDefs authentication,
                               UserRepository userRepository) {
        this.stepDefs = stepDefs;
        this.authentication = authentication;
        this.userRepository = userRepository;
    }

    // Other scenarios log in as the admin with the default password, so it is restored after a custom one is used
    @After
    public void restoreDefaultAdminPassword() {
        if (adminPasswordChanged) {
            saveAdmin(DEFAULT_PASSWORD);
            adminPasswordChanged = false;
        }
    }

    @Given("an admin user exists with username {string} and password {string}")
    public void anAdminUserExistsWithUsernameAndPassword(String username, String password) {
        if (ADMIN_USERNAME.equals(username) && !DEFAULT_PASSWORD.equals(password)) {
            adminPasswordChanged = true;
        }
        saveAdmin(username, password);
    }

    @When("I login with {string} and password {string}")
    public void iLoginWithAndPassword(String username, String password) throws Exception {
        AuthenticationStepDefs.currentUsername = username;
        AuthenticationStepDefs.currentPassword = password;
        stepDefs.result = authentication.identity();
    }

    @Then("I am authenticated as {string}")
    public void iAmAuthenticatedAs(String username) throws Exception {
        authentication.identity()
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is(username)));
    }

    @And("I am not authenticated")
    public void iAmNotAuthenticated() throws Exception {
        authentication.identity().andExpect(status().is4xxClientError());
    }

    @When("I logout")
    public void iLogout() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/logout")
                    .accept(MediaType.TEXT_HTML)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
        // The API is stateless: logging out means the client discards its credentials
        AuthenticationStepDefs.currentUsername = AuthenticationStepDefs.currentPassword = null;
    }

    @And("I am redirected to the login or home page")
    public void iAmRedirectedToTheLoginOrHomePage() throws Exception {
        stepDefs.result.andExpect(redirectedUrl("/"));
    }

    @And("I cannot access protected resources")
    public void iCannotAccessProtectedResources() throws Exception {
        AuthenticationStepDefs.assertAuthenticationChallenge(authentication.identity());
    }

    private void saveAdmin(String password) {
        saveAdmin(ADMIN_USERNAME, password);
    }

    private void saveAdmin(String username, String password) {
        User admin = userRepository.findById(username).orElseGet(() -> {
            User user = new User();
            user.setId(username);
            user.setFullName(username);
            user.setEmail(username + "@sample.app");
            return user;
        });
        admin.setAdmin(true);
        admin.setPassword(password);
        admin.encodePassword();
        userRepository.save(admin);
    }
}
