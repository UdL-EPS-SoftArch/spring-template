package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Announcement;
import cat.udl.eps.softarch.demo.domain.Offer;
import cat.udl.eps.softarch.demo.domain.Request;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.OfferRepository;
import cat.udl.eps.softarch.demo.repository.RequestRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hibernate.internal.util.ZonedDateTimeComparator;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.http.MediaType;


import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class RegisterRequestStepDefs {

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @And("There is an offer created")
    public void thereIsAnOfferCreated() throws Exception {
        Offer offer = new Offer();
        offer.setName("croqueta");
        offer.setPrice(new BigDecimal(50));
        offer.setDescription("las croquestas mas ricas de la mama");
/*
        Optional<User> users = userRepository.findById("mama");
        if(users.isPresent()) {
            User mama = users.get();
            offer.setOffererUser(mama);
        }
*/

        User mama = new User();
        mama.setEmail("mama@cocina.casa");
        mama.setUsername("mama");
        mama.setPassword("password");



     //   offerRepository.save(offer);

        // Pruebas random
        long num = requestRepository.count();
        boolean thereAreOffers = num > 0;

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(offer))
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());

        //Assert.assertTrue(thereAreOffers);
        Assert.assertEquals(1, offerRepository.count());
    }


    @When("I Create a new request")
    public void iCreateANewRequest() throws Exception {
        Request request = new Request();

        request.setName("croqueta");
        request.setPrice(new BigDecimal(50));
        request.setDescription("las croquestas mas ricas de la mama");

        User nene = new User();
        nene.setEmail("nene@cocina.casa");
        nene.setUsername("nene");
        nene.setPassword("password");

        request.setRequester(nene);

        //requestRepository.save(request);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());


    }

    @Then("There is a request created")
    public void thereIsARequestCreated() {
        Assert.assertEquals(1, requestRepository.count());
    }


    @And("There is an offer created with name {string}, price {int}, description {string} and offerer named {string}")
    public void thereIsAnOfferCreatedWithNamePriceDescriptionAndOffererNamed(String name, int price, String description, String offererName) {
        Offer offer = new Offer();
        offer.setName(name);
        offer.setPrice(new BigDecimal(price));
        offer.setDescription(description);
        User offerer = new User();
        offerer.setUsername(offererName);
        offerer.setPassword("password");
        offerer.setEmail(offererName + "@gmail.com");
        offer.setOffererUser(offerer);
        offerRepository.save(offer);
        Assert.assertEquals(1, offerRepository.count());
    }
}
