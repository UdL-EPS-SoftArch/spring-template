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

    @Then("Execute the yarrrml parser with the yaml file")
    public void writeTheYamlFile() {
        ExternalCommandExecutor executor = new ExternalCommandExecutor();
        executor.executeYARRRMLParser();
    }
}
