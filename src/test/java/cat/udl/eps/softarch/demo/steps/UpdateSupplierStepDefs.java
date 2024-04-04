package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Mapping;
import cat.udl.eps.softarch.demo.repository.MappingRepository;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UpdateSupplierStepDefs {
    final StepDefs stepDefs;

    public UpdateSupplierStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @When("I update the email of supplier {string} to {string}")
    public void iChangeTheEmailOfSupplierTo(String username, String email) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                patch("/suppliers/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new JSONObject().put("email", email)).toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate())
        ).andDo(print());
    }

    @And("It has been updated the email of supplier {string} to {string}")
    public void itHasBeenUpdatedTheEmailOfSupplierTo(String username, String email) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/suppliers/{username}", username)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.email", is(email)));
    }
}
