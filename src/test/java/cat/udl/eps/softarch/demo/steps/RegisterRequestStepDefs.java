package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Announcement;
import cat.udl.eps.softarch.demo.domain.Offer;
import cat.udl.eps.softarch.demo.domain.Request;
import cat.udl.eps.softarch.demo.domain.User;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

import java.math.BigDecimal;

public class RegisterRequestStepDefs {

    private StepDefs stepDefs;

    @When("^I create a new request with name \"([^\"]*)\", price \"([^\"]*)\", description \"([^\"]*)\" and requester \"([^\"]*)\"$")
    public void iCreateANewRequestWithNamePriceDescriptionAndRequester(String name, BigDecimal price, String description, User requester){

    }

    @And("Exists an offer with name \"([^\"]*)\", price \"([^\"]*)\", description \"([^\"]*)\" and offererUser \"([^\"]*)\"$")
    public void existsAnOfferWithNamePriceDescriptionAndOffererUser(String name, BigDecimal price, String description, User offererUser) {
        Offer offer = new Offer();
        offer.setName(name);
        offer.setPrice(price);
        offer.setDescription(description);
        offer.setOffererUser(offererUser);
    }


}
