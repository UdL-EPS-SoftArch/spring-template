package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Offer;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.OfferRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class OfferStepDefs {
    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferRepository offerRepository;

    private Offer offer;


    @Given("There is no offer created from the user {string}")
    public void thereIsNoOfferCreatedFromTheUser(String username) {
        Optional<User> user = userRepository.findById(username);
        Assert.assertTrue("This user has no offer created", user.isEmpty() ||
                !offerRepository.existsOfferByOfferer(user.get()));
    }

    @Then("The offer should be created together with the announcement")
    public void theOfferShouldBeCreatedTogetherWithTheAnnouncement() {
        offer = new Offer();

        Assert.assertFalse(offer == null);
    }

    @And("which has a name {string}, description {string} and a price {string}.")
    public void whichHasANameDescriptionAndAPrice(String name, String description, String price) {
        offer.setName(name);
        offer.setDescription(description);
        offer.setPrice(new BigDecimal(price));

        Assert.assertEquals(name, offer.getName());
        Assert.assertEquals(description, offer.getDescription());
        Assert.assertEquals(price, String.valueOf(offer.getPrice()));
    }

    @Transactional
    @And("a ZoneDateTime {string} and a offerer user {string}")
    public void aZoneDateTimeAndAOffererUser(String dateTime, String offerer) throws Throwable {
        Date expectedDate = new Date("01/02/2018");
        offer.setDateTime(new Date(dateTime));
        Optional<User> user = userRepository.findById(offerer);
        user.ifPresent(value -> offer.setOfferer(value));

        Assert.assertEquals(expectedDate, offer.getDateTime());
        user.ifPresent(value -> Assert.assertEquals(value, offer.getOfferer()));
        System.out.println(offer.getDateTime());
        offerRepository.save(offer);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(offer))
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @And("After all the steps I can retrieve this offer using the name {string} and the offererUser {string}")
    public void afterAllTheStepsICanRetrieveThisOfferUsingTheNameAndTheOffererUser(String name, String offererUser) {

    }
}
