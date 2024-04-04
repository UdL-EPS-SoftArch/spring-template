package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Column;
import cat.udl.eps.softarch.demo.domain.Mapping;
import cat.udl.eps.softarch.demo.repository.ColumnRepository;
import cat.udl.eps.softarch.demo.repository.MappingRepository;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class UpdateColumnStepDefs {
    final StepDefs stepDefs;
    final MappingRepository mappingRepository;

    final ColumnRepository columnRepository;
    public UpdateColumnStepDefs(StepDefs stepDefs, MappingRepository mappingRepository, ColumnRepository columnRepository) {
        this.stepDefs = stepDefs;
        this.mappingRepository = mappingRepository;
        this.columnRepository = columnRepository;
    }

    @When("I update the column with title {string} to new title {string} for the mapping with id {long}")
    public void iUpdateTheColumnWithTitleToNewTitleForTheMapping(String title, String newTitle, Long mappingId) throws Exception {
        Optional<Mapping> mapping = mappingRepository.findById(mappingId);
        Column column = columnRepository.findByTitleAndColumnBelongsTo(title, mapping.get());
        column.setTitle(newTitle);
        stepDefs.result = stepDefs.mockMvc.perform(
                patch("/columns/" + column.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(column))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("I update the column with title {string} to new ontology uri {string} for the mapping with id {long}")
    public void iUpdateTheColumnWithTitleToNewOntologyUriForTheMappingWithId(String title, String ontologyURI, Long mappingId) throws Exception {
        Optional<Mapping> mapping = mappingRepository.findById(mappingId);
        Column column = columnRepository.findByTitleAndColumnBelongsTo(title, mapping.get());
        column.setOntologyURI(ontologyURI);
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/columns/" + column.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(column))
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("I update the column with title {string} to new ontology type {string} for the mapping with id {long}")
    public void iUpdateTheColumnWithTitleToNewOntologyForTheMappingWithId(String title, String ontologyType, Long mappingId) throws Exception {
        Optional<Mapping> mapping = mappingRepository.findById(mappingId);
        Column column = columnRepository.findByTitleAndColumnBelongsTo(title, mapping.get());
        column.setOntologyType(ontologyType);
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/columns/" + column.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(column))
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("I update the column with title {string} to new data type {string} for the mapping with id {long}")
    public void iUpdateTheColumnWithTitleToNewDataTypeForTheMappingWithId(String title, String dataType, Long mappingId) throws Exception {
        Optional<Mapping> mapping = mappingRepository.findById(mappingId);
        Column column = columnRepository.findByTitleAndColumnBelongsTo(title, mapping.get());
        column.setDataType(dataType);
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/columns/" + column.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(column))
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }
}
