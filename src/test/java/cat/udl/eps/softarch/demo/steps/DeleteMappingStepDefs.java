package cat.udl.eps.softarch.demo.steps;

import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class DeleteMappingStepDefs {
    final StepDefs stepDefs;

    public DeleteMappingStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }


    @When("I delete the mapping with id {string}")
    public void iDeleteTheMappingWithId(String id) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        delete("/mappings/" + id)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }
}
