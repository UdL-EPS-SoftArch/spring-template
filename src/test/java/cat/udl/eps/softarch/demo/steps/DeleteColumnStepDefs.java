package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Column;
import cat.udl.eps.softarch.demo.domain.Mapping;
import cat.udl.eps.softarch.demo.repository.ColumnRepository;
import cat.udl.eps.softarch.demo.repository.MappingRepository;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class DeleteColumnStepDefs {
    final StepDefs stepDefs;
    final MappingRepository mappingRepository;
    final ColumnRepository columnRepository;

    public DeleteColumnStepDefs(StepDefs stepDefs, MappingRepository mappingRepository,
                                ColumnRepository columnRepository) {
        this.stepDefs = stepDefs;
        this.mappingRepository = mappingRepository;
        this.columnRepository = columnRepository;
    }


    @When("I delete the column with title {string} from the mapping {string}")
    public void iDeleteTheColumnWithTitleFromTheMapping(String columnTitle, String mappingTitle) throws Exception {
        Mapping mapping = mappingRepository.findByTitle(mappingTitle).get(0);
        Column column = columnRepository.findByTitleAndColumnBelongsTo(columnTitle, mapping);

        stepDefs.result = stepDefs.mockMvc.perform(
                        delete("/columns/" + column.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }
}
