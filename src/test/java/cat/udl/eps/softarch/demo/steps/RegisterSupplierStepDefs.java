package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Supplier;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.http.MediaType;

import org.json.JSONObject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegisterSupplierStepDefs {
    final StepDefs stepDefs;
    final SupplierRepository supplierRepository;

    RegisterSupplierStepDefs(StepDefs stepDefs, SupplierRepository supplierRepository) {
        this.stepDefs = stepDefs;
        this.supplierRepository = supplierRepository;
    }


    @Given("There is a registered supplier with username {string}, email {string} and password {string}")
    public void thereIsARegisteredSupplierWithUsernameAndPasswordAndEmail(String username, String email, String password) {
        if (supplierRepository.findByUsernameContaining(username).isEmpty()) {
            Supplier supplier = new Supplier();
            supplier.setEmail(email);
            supplier.setUsername(username);
            supplier.setPassword(password);
            supplier.encodePassword();
            supplierRepository.save(supplier);
        }
    }

    @When("I register a new supplier with username {string}, email {string} and password {string}")
    public void iRegisterANewSupplierWithUsernameEmailAndPassword(String username, String email, String password) throws Throwable {
        Supplier supplier = new Supplier();
        supplier.setUsername(username);
        supplier.setEmail(email);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(
                                stepDefs.mapper.writeValueAsString(supplier)
                        ).put("password", password).toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @Given("There is no registered supplier with username {string}")
    public void thereIsNoRegisteredSupplierWithUsername(String username) {
        Assert.assertTrue("Supplier \""
                        +  username + "\"shouldn't exist",
                supplierRepository.findByUsernameContaining(username).isEmpty());
    }

    @And("It has not found a supplier with username {string}")
    public void itHasNotFoundASupplierWithUsername(String username) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/suppliers/{username}", username)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andExpect(status().isNotFound());
    }
}
