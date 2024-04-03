package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.repository.MappingRepository;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class DeleteMappingStepDefs {
    final StepDefs stepDefs;
    final MappingRepository mappingRepository;
    final SupplierRepository supplierRepository;


    public DeleteMappingStepDefs(StepDefs stepDefs, MappingRepository mappingRepository, SupplierRepository supplierRepository) {
        this.stepDefs = stepDefs;
        this.mappingRepository = mappingRepository;
        this.supplierRepository = supplierRepository;
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
