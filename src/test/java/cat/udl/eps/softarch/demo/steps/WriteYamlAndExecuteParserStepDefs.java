package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.utils.ExternalCommandExecutor;
import io.cucumber.java.en.Then;

public class WriteYamlAndExecuteParserStepDefs {

    @Then("Execute the yarrrml parser with the yaml file")
    public void writeTheYamlFile() {
        ExternalCommandExecutor executor = new ExternalCommandExecutor();
        executor.executeYARRRMLParser();
    }
}
