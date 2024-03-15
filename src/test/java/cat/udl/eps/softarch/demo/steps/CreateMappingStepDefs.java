package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Mapping;
import cat.udl.eps.softarch.demo.domain.Supplier;
import cat.udl.eps.softarch.demo.repository.MappingRepository;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class CreateMappingStepDefs {
    final StepDefs stepDefs;
    final MappingRepository mappingRepository;

    final SupplierRepository supplierRepository;
    private String newResourceUri;

    public CreateMappingStepDefs(StepDefs stepDefs, MappingRepository mappingRepository, SupplierRepository supplierRepository) {
        this.stepDefs = stepDefs;
        this.mappingRepository = mappingRepository;
        this.supplierRepository = supplierRepository;
    }

    @When("I create a new mapping with name {string}")
    public void iCreateANewMappingWithTitleAndDescription(String name) throws Throwable {
        Mapping mapping = new Mapping();
        mapping.setTitle(name);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/mappings")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(stepDefs.mapper.writeValueAsString(mapping))
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());

    }

    @And("It has been created a new mapping with name {string} and is owned by {string}")
    public void itHasBeenCreatedANewMappingWithNameAndIsOwnedBy(String name, String username) throws Throwable {
        JSONObject response = new JSONObject(stepDefs.result.andReturn().getResponse().getContentAsString());
        String titleHref = response.getJSONObject("_links").getJSONObject("self").getString("href");

        stepDefs.result = stepDefs.mockMvc.perform(
                        get(titleHref)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.title", is(name)));

        JSONObject MappingResponse = new JSONObject(stepDefs.result.andReturn().getResponse().getContentAsString());
        String providedByHref = MappingResponse.getJSONObject("_links").getJSONObject("providedBy").getString("href");

        assertProvidedByEqualsToExpectedUser(providedByHref, username);
    }

    public void assertProvidedByEqualsToExpectedUser(String providedByHref, String expectedUsername) throws Throwable {
        stepDefs.mockMvc.perform(
                        get(providedByHref)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(expectedUsername)));
    }
}
