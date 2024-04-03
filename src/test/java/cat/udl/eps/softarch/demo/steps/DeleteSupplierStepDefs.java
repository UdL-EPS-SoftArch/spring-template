package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.repository.MappingRepository;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteSupplierStepDefs {
    final StepDefs stepDefs;
    final MappingRepository mappingRepository;
    final SupplierRepository supplierRepository;

    final UserRepository userRepository;

    public DeleteSupplierStepDefs(StepDefs stepDefs, MappingRepository mappingRepository, SupplierRepository supplierRepository, UserRepository userRepository) {
        this.stepDefs = stepDefs;
        this.mappingRepository = mappingRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    @When("I delete the supplier with username {string}")
    public void iDeleteTheSupplierWithUsername(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        delete("/suppliers/{username}", username)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }


    @And("It does not exist a supplier with username {string}")
    public void itDoesNotExistASupplierWithUsername(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/suppliers/{username}", username)
                                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @And("It still exists a supplier with username {string}")
    public void itStillExistsASupplierWithUsername(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/suppliers/{username}", username)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(username)));
    }
}
