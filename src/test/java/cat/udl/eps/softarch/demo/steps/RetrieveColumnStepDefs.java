package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Column;
import cat.udl.eps.softarch.demo.domain.Mapping;
import cat.udl.eps.softarch.demo.repository.ColumnRepository;
import cat.udl.eps.softarch.demo.repository.MappingRepository;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class RetrieveColumnStepDefs {
    final StepDefs stepDefs;
    final MappingRepository mappingRepository;

    final ColumnRepository columnRepository;

    public RetrieveColumnStepDefs(StepDefs stepDefs, MappingRepository mappingRepository, ColumnRepository columnRepository) {
        this.stepDefs = stepDefs;
        this.mappingRepository = mappingRepository;
        this.columnRepository = columnRepository;
    }

    @When("I retrieve the column with title {string} for the mapping {string}")
    public void iRetrieveTheColumnWithTitleForTheMapping(String columnTitle, String mappingTitle) throws Exception {
        Mapping mapping = mappingRepository.findByTitle(mappingTitle).get(0);
        Column column = columnRepository.findByTitleAndColumnBelongsTo(columnTitle, mapping);

        Long columnId;
        if (column == null) {
            columnId = -1L;
        } else {
            columnId = column.getId();
        }

        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/columns/" + columnId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(column))
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @And("The column retrieved has ontology uri {string}")
    public void theColumnWithTitleForTheMappingHasOntologyUri(String ontologyUri) throws Exception {
        stepDefs.result.andExpect(jsonPath("$.ontologyURI").value(ontologyUri));

    }

    @And("The column retrieved has ontology type {string}")
    public void theColumnRetrievedHasOntology(String ontologyType) throws Exception {
        stepDefs.result.andExpect(jsonPath("$.ontologyType").value(ontologyType));
    }

    @And("The column retrieved has data type {string}")
    public void theColumnRetrievedHasDataType(String ontologyType) throws Exception {
        stepDefs.result.andExpect(jsonPath("$.dataType").value(ontologyType));
    }
}
