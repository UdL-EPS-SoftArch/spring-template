package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Announcement;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.OfferRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

public class OfferStepDefs {
    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferRepository offerRepository;

    private User currentUser;




    @Given("There is no offer created from the user {string}")
    public void thereIsNoOfferCreatedFromTheUser(String username) {
        currentUser = new User();
        currentUser.setEmail("example@mail.com");
        currentUser.setUsername("user");
        currentUser.setPassword("password");
        currentUser.encodePassword();
        userRepository.save(currentUser);

        Assert.assertTrue("User \""
                        +  username + "\"exists in the database",
                userRepository.existsById(username));

        Assert.assertTrue("This user has no offer created", offerRepository.existsOfferByOffererUser(username));
    }

    @And("I'm logged in with user {string} and password {string}")
    public void iMLoggedInWithUserAndPassword(String username, String password) {

    }

    @Then("The offer should be created together with the announcement")
    public void theOfferShouldBeCreatedTogetherWithTheAnnouncement() {

    }

    @And("which has a name {string}, description {string} and a price {string}.")
    public void whichHasANameDescriptionAndAPrice(String name, String description, String price) {

    }

    @And("a ZoneDateTime {string} and a offerer user {string}")
    public void aZoneDateTimeAndAOffererUser(String dateTime, String offererUser) {

    }

    @And("After all the steps I can retrieve this offer using the name {string} and the offererUser {string}")
    public void afterAllTheStepsICanRetrieveThisOfferUsingTheNameAndTheOffererUser(String name, String offererUser) {

    }
}
