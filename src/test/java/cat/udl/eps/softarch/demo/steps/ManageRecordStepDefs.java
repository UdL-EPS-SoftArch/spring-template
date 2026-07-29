package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Record;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.RecordRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ManageRecordStepDefs {
    private final StepDefs stepDefs;
    private final UserRepository userRepository;
    private final RecordRepository recordRepository;

    public ManageRecordStepDefs(StepDefs stepDefs, UserRepository userRepository, 
        RecordRepository recordRepository) {
        this.stepDefs = stepDefs;
        this.userRepository = userRepository;
        this.recordRepository = recordRepository;
    }

    @When("^I create a new record with name \"([^\"]*)\"$")
    public void iCreateANewRecordWithNameOwnedBy(String name) throws Throwable {
        Record record = new Record();
        record.setName(name);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/records")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(record))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @And("^The new record is owned by \"([^\"]*)\"$")
    public void theNewRecordIsOwnedBy(String username) throws Throwable {
        String newRecordUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
        stepDefs.result = stepDefs.mockMvc.perform(
                get(newRecordUri + "/ownedBy")
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$.username", is(username)));
    }

    @When("^I retrieve the record with name \"([^\"]*)\"$")
    public void iRetrieveRecordByName(String name) throws Throwable {
        Record record = recordRepository.findByName(name).stream().findFirst().orElseThrow();
        stepDefs.result = stepDefs.mockMvc.perform(
                get(record.getUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @Then("^The retrieved record has name \"([^\"]*)\"$")
    public void theRetrieveRecordHasName(String name) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.name", is(name)));
    }

    @And("^The list of records owned by \"([^\"]*)\" includes one named \"([^\"]*)\"$")
    public void itHasBeenCreatedAUserWithUsername(String username, String resourceName) throws Throwable {
        User owner = userRepository.findById(username).orElseThrow();
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/records/search/findByOwnedBy?user={userUri}", owner.getUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andExpect(jsonPath("$._embedded.records[*].name", hasItem(is(resourceName))));
    }
}
