package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.utils.ExternalCommandExecutor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ExternalCommandStepDefs {
    final StepDefs stepDefs;

    public ExternalCommandStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @Given("A rules file name {string}")
    public void aRulesFileName(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("I execute the command with the file name {string} and the output file name {string}")
    public void iExecuteTheCommand(String inputFileName, String outputFileName) throws Throwable {

        ExternalCommandExecutor externalCommandExecutor = new ExternalCommandExecutor();
        externalCommandExecutor.executeYARRRMLParser(inputFileName, outputFileName);
    }

    @Then("The file {string} should be generated")
    public void theFileShouldBeGenerated(String fileGenerated) {

    }
}
