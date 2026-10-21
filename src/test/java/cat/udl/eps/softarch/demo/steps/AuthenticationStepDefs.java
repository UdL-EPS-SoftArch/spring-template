package cat.udl.eps.softarch.demo.steps;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class AuthenticationStepDefs {

    public static String currentUsername;
    public static String currentPassword;

    private final StepDefs stepDefs;

    public AuthenticationStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @Before
    public void setup() {
        // Clear authentication credentials at the start of every test.
        AuthenticationStepDefs.currentPassword = "";
        AuthenticationStepDefs.currentUsername = "";
    }

    static RequestPostProcessor authenticate() {
        return currentUsername!=null ? httpBasic(currentUsername, currentPassword) : anonymous();
    }

    // The API is stateless, so an unauthenticated client is told to authenticate and the client does the redirecting
    static void assertAuthenticationChallenge(ResultActions result) throws Exception {
        result.andExpect(status().isUnauthorized())
            .andExpect(header().exists(HttpHeaders.WWW_AUTHENTICATE));
    }

    @Given("^I login as \"([^\"]*)\" with password \"([^\"]*)\"$")
    public void iLoginAsWithPassword(String username, String password) throws Exception {
        AuthenticationStepDefs.currentUsername = username;
        AuthenticationStepDefs.currentPassword = password;
        stepDefs.result = identity();
    }

    @Given("^I'm not logged in$")
    public void iMNotLoggedIn() {
        currentUsername = currentPassword = null;
    }

    ResultActions identity() throws Exception {
        return stepDefs.mockMvc.perform(
                get("/identity")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(authenticate()))
            .andDo(print());
    }
}
