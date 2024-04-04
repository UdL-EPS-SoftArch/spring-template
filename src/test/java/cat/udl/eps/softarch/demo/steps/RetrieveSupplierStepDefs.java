package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Mapping;
import cat.udl.eps.softarch.demo.repository.MappingRepository;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class RetrieveSupplierStepDefs {
    final StepDefs stepDefs;
    public RetrieveSupplierStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @When("I list all the existing suppliers")
    public void iListAllTheExistingSuppliers() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/suppliers")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @And("There are {int} suppliers being retrieved")
    public void thereAreMappingsBeingRetrieved(int numSuppliers) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$._embedded.suppliers", hasSize(numSuppliers)));
    }

    @When("I retrieve the supplier with username {string}")
    public void iRetrieveTheSupplierWithUsername(String username) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/suppliers/{username}", username)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @And("It has been retrieve the supplier with username {string}")
    public void itHasBeenRetrieveTheSupplierWithUsername(String username) throws Exception {
        stepDefs.result.andExpect(jsonPath("$.id").value(username));
    }
}
