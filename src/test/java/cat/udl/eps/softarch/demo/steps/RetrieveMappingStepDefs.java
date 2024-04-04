package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Mapping;
import cat.udl.eps.softarch.demo.domain.Supplier;
import cat.udl.eps.softarch.demo.repository.MappingRepository;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class RetrieveMappingStepDefs {
    final StepDefs stepDefs;
    final MappingRepository mappingRepository;
    final SupplierRepository supplierRepository;

    public RetrieveMappingStepDefs(StepDefs stepDefs, MappingRepository mappingRepository, SupplierRepository supplierRepository) {
        this.stepDefs = stepDefs;
        this.mappingRepository = mappingRepository;
        this.supplierRepository = supplierRepository;
    }

    @Given("There is a created mapping with the title {string} and provided by {string}")
    public void thereIsACreatedMappingWithTitle(String title, String supplierName) {
        Mapping mapping = new Mapping();
        mapping.setTitle(title);

        mapping.setProvidedBy(supplierRepository.findById(supplierName).get());
        mappingRepository.save(mapping);

    }

    @When("I list all the existing mapping")
    public void iListAllTheExistingMapping() throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/mappings")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
               .andDo(print());
    }

    @And("There are {int} mappings being retrieved")
    public void thereAreMappingsBeingRetrieved(int numMappings) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$._embedded.mappings", hasSize(numMappings)));
    }

    @When("I retrieve the mapping with id {string}")
    public void iRetrieveTheMappingWithId(String id) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/mappings/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("I list all the existing mapping with title containing {string}")
    public void iListAllTheExistingMappingWithTitleContaining(String title) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/mappings/search/findByTitleContaining?title=" + title)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }


    @When("I retrieve the mapping with title {string}")
    public void iRetrieveTheMappingWithTitle(String title) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/mappings/search/findByTitle?title=" + title)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }
}
