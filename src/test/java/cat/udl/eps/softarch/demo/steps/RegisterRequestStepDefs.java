package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Announcement;
import cat.udl.eps.softarch.demo.domain.Offer;
import cat.udl.eps.softarch.demo.domain.Request;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.OfferRepository;
import cat.udl.eps.softarch.demo.repository.RequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hibernate.internal.util.ZonedDateTimeComparator;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.springframework.http.MediaType;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class RegisterRequestStepDefs {

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private StepDefs stepDefs;

    @And("There is an offer created")
    public void thereIsAnOfferCreated() throws Exception {
        Offer offer = new Offer();
        offer.setName("croqueta");
        offer.setPrice(new BigDecimal(50));
        offer.setDescription("las croquestas mas ricas de la mama");

        User mama = new User();
        mama.setEmail("mama@cocina.casa");
        mama.setUsername("mama");
        mama.setPassword("password");

        offer.setOffererUser(mama);

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
}
