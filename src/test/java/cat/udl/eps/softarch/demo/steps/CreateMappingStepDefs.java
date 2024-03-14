package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Mapping;
import cat.udl.eps.softarch.demo.repository.MappingRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class CreateMappingStepDefs {
    final StepDefs stepDefs;
    final MappingRepository mappingRepository;
    private String newResourceUri;

    public CreateMappingStepDefs(StepDefs stepDefs, MappingRepository mappingRepository) {
        this.stepDefs = stepDefs;
        this.mappingRepository = mappingRepository;
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
    public void itHasBeenCreatedANewMappingWithNameAndIsOwnedBy(String name, String username) {

    }
}
