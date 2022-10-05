package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Message;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.MessageRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;

public class MessageStepDefs {
    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private MessageRepository messageRepository;
    private UserRepository userRepository;

    @Given("^I create message with id (\\d+)")
    public void iCreateMessageWithId(Integer id){
        Long ident = Long.valueOf(id);
        Assert.assertFalse("Id \"" + id + "\"shouldn't exist",messageRepository.existsById(ident));
    }
    @When("I send the message whit id (\\d+) and the text \"([^\"]*)\"")
    public void iSendTheMessageWhitIdAndTheText(Integer id, String text){
        Message message = new Message();
        Long ident = Long.valueOf(id);

        ZonedDateTime date = ZonedDateTime.now();

        message.setId(ident);
        message.setWhen(date);
        message.setText(text);

    }
}
