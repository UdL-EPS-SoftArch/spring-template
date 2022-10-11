package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Message;
import cat.udl.eps.softarch.demo.repository.MessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.ZonedDateTime;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


public class MessageStepDefs {

    @Autowired
    private StepDefs stepDefs;
    @Autowired
    private MessageRepository messageRepository;




    @Given("The message with id {long} doesn't exist")
    public void theMessageWithIdDoesnTExist(long ident) {

        Assert.assertFalse("Id \"" + ident + "\"shouldn't exist",messageRepository.existsById(ident));

    }

    @When("I send the message whit id {long} and text {string}")
    public void iSendTheMessageWhitIdAndText(long ident, String text) throws Exception {
        ZonedDateTime date = ZonedDateTime.now();
        Message message = new Message();
        message.setId(ident);
        message.setWhen(date);
        message.setText(text);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(message))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate())).andDo(print());
    }


}
