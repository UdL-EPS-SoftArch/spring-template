package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Message;
import cat.udl.eps.softarch.demo.repository.MessageRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.hibernate.type.ZonedDateTimeType;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


public class MessageStepDefs {

    @Autowired
    private StepDefs stepDefs;
    @Autowired
    private MessageRepository messageRepository;




    @And("The message with id {long} doesn't exist")
    public void theMessageWithIdDoesntExist(long ident) {

        Assert.assertFalse("Id \"" + ident + "\"shouldn't exist",messageRepository.existsById(ident));

    }


    @When("I send the message with date {string} and text {string}")
    public void iSendTheMessageWithDateAndText(String date, String text) throws Exception{
        final DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        ZonedDateTime dated = ZonedDateTime.parse(date ,formatter);
        Message message = new Message();
        //message.setId(ident);
        message.setWhen(dated);
        message.setText(text);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(message))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate())).andDo(print());
    }
}
