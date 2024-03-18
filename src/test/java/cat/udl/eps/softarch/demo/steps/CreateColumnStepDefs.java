package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Column;
import cat.udl.eps.softarch.demo.domain.Mapping;
import cat.udl.eps.softarch.demo.repository.ColumnRepository;
import cat.udl.eps.softarch.demo.repository.MappingRepository;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayOutputStream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class CreateColumnStepDefs {
    final StepDefs stepDefs;
    final MappingRepository mappingRepository;

    final SupplierRepository supplierRepository;

    final ColumnRepository columnRepository;
    private String newResourceUri;

    public CreateColumnStepDefs(StepDefs stepDefs, MappingRepository mappingRepository,
                                SupplierRepository supplierRepository, ColumnRepository columnRepository) {
        this.stepDefs = stepDefs;
        this.mappingRepository = mappingRepository;
        this.supplierRepository = supplierRepository;
        this.columnRepository = columnRepository;
    }

    @When("I create a new column with title {string} and data type {string} for the mapping {string}")
    public void iCreateANewColumnWithTitleAndDescription(String title, String type, String mappingTitle) throws Throwable {
        Mapping mapping = mappingRepository.findByTitle(mappingTitle).get(0);
        Column column = new Column();
        column.setTitle(title);
        column.setDataType(type);
        column.setColumnBelongsTo(mapping);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/columns")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(stepDefs.mapper.writeValueAsString(column))
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @Then("It has been created a new column with title {string} and data type {string} for the mapping {string}")
    public void itHasBeenCreatedANewColumnWithTitleAndDataTypeForTheMapping(String title, String type, String mappingTitle)
            throws Throwable {
        Mapping mapping = mappingRepository.findByTitle(mappingTitle).get(0);
        Column column = columnRepository.findByTitleAndColumnBelongsTo(title, mapping);

        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/columns/{id}", column.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.dataType", is(type)));

    }
}
