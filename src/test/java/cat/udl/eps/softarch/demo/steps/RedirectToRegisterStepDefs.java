package cat.udl.eps.softarch.demo.steps;

import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.EventRepository;
import cat.udl.eps.softarch.demo.repository.SubcategoryRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class RedirectToRegisterStepDefs {
    private static final String ORGANIZER_USERNAME = "organizer";

    private final StepDefs stepDefs;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final SubcategoryRepository subcategoryRepository;

    public RedirectToRegisterStepDefs(StepDefs stepDefs, EventRepository eventRepository,
                                      UserRepository userRepository, SubcategoryRepository subcategoryRepository) {
        this.stepDefs = stepDefs;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @Given("an event titled {string} exists and is publicly available")
    public void anEventExistsAndIsPubliclyAvailable(String title) {
        StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .filter(event -> title.equals(event.getTitle()))
                .forEach(eventRepository::delete);

        Event event = new Event();
        event.setTitle(title);
        event.setDescription("Meet other Erasmus students");
        event.setAddress("City park");
        event.setDateTime(LocalDateTime.of(2030, 11, 10, 16, 0));
        event.setMaxParticipants(20);
        event.setEstimatedCost(new BigDecimal("5.00"));
        event.setSubcategory(subcategoryRepository.findAll().iterator().next());
        event.setCreator(organizer());
        eventRepository.save(event);
    }

    @When("I try to join the event titled {string}")
    public void iTryToJoinTheEvent(String title) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                post(eventTitled(title).getUri() + "/participants")
                    .contentType(MediaType.parseMediaType("text/uri-list"))
                    .content("http://localhost/users/visitor")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @Then("I am redirected to the registration page")
    public void iAmRedirectedToTheRegistrationPage() throws Exception {
        AuthenticationStepDefs.assertAuthenticationChallenge(stepDefs.result);
    }

    @And("I see a message asking me to register or log in")
    public void iSeeAMessageAskingMeToRegisterOrLogIn() throws Exception {
        AuthenticationStepDefs.assertAuthenticationChallenge(stepDefs.result);
    }

    @And("I am not registered for the event titled {string}")
    public void iAmNotRegisteredForTheEvent(String title) {
        assertTrue(eventTitled(title).getParticipants().isEmpty(), "Nobody should be registered for " + title);
    }

    private Event eventTitled(String title) {
        return StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .filter(event -> title.equals(event.getTitle()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No event titled \"" + title + "\" exists"));
    }

    private User organizer() {
        return userRepository.findById(ORGANIZER_USERNAME).orElseGet(() -> {
            User user = new User();
            user.setId(ORGANIZER_USERNAME);
            user.setFullName(ORGANIZER_USERNAME);
            user.setEmail(ORGANIZER_USERNAME + "@sample.app");
            user.setPassword("password");
            user.encodePassword();
            return userRepository.save(user);
        });
    }
}
