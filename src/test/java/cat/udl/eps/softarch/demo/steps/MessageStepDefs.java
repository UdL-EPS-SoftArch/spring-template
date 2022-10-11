package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Message;
import cat.udl.eps.softarch.demo.repository.MessageRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.Date;

public class MessageStepDefs {

    //private StepDefs stepDefs;
    @Autowired
    private MessageRepository messageRepository;


    @Given("The message with id {long} doesn't exist")
    public void theMessageWithIdDoesnTExist(long ident) {

        Assert.assertFalse("Id \"" + ident + "\"shouldn't exist",messageRepository.existsById(ident));

    }

    @When("I send the message whit id {long} and text {string}")
    public void iSendTheMessageWhitIdAndText(long ident, String text) {
        ZonedDateTime date = ZonedDateTime.now();
        if(!messageRepository.existsById(ident)){
            Message message = new Message();

            message.setWhen(date);
            message.setText(text);
            messageRepository.save(message);
        }
    }
}
