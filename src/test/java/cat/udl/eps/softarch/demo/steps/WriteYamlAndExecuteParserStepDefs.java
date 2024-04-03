package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Mapping;
import cat.udl.eps.softarch.demo.repository.ColumnRepository;
import cat.udl.eps.softarch.demo.repository.MappingRepository;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import cat.udl.eps.softarch.demo.utils.ExternalCommandExecutor;
import cat.udl.eps.softarch.demo.utils.YamlGenerator;
import io.cucumber.java.en.Then;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

public class WriteYamlAndExecuteParserStepDefs {
    final StepDefs stepDefs;

    protected MockMvc mockMvc;

    protected ResultActions result;

    final MappingRepository mappingRepository;
    final WebApplicationContext wac;

    final SupplierRepository supplierRepository;

    final ColumnRepository columnRepository;

    public static Mapping mappingFile;

    public WriteYamlAndExecuteParserStepDefs(StepDefs stepDefs, MappingRepository mappingRepository, WebApplicationContext wac, SupplierRepository supplierRepository, ColumnRepository columnRepository) {
        this.stepDefs = stepDefs;
        this.mappingRepository = mappingRepository;
        this.wac = wac;
        this.supplierRepository = supplierRepository;
        this.columnRepository = columnRepository;
    }

    @Then("Execute the yarrrml parser with the yaml file")
    public void writeTheYamlFile() throws IOException, InterruptedException {
        ExternalCommandExecutor executor = new ExternalCommandExecutor();
        executor.executeYARRRMLParser();
    }
}
