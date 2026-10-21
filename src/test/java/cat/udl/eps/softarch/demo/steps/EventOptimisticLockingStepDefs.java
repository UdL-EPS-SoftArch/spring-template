package cat.udl.eps.softarch.demo.steps;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.repository.EventRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.StreamSupport;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class EventOptimisticLockingStepDefs {
    private final StepDefs stepDefs;
    private final EventRepository eventRepository;

    private String eventUri;
    private String initialEtag;
    private String initialBody;
    private ResultActions client1Result;
    private ResultActions client2Result;
    private Event entityA;
    private Event entityB;
    private RuntimeException saveException;

    public EventOptimisticLockingStepDefs(StepDefs stepDefs, EventRepository eventRepository) {
        this.stepDefs = stepDefs;
        this.eventRepository = eventRepository;
    }

    @Given("two clients have loaded the event titled {string}")
    public void twoClientsHaveLoadedTheEventTitled(String title) throws Throwable {
        Event event = StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .filter(e -> title.equals(e.getTitle()))
                .findFirst()
                .orElseThrow();
        eventUri = event.getUri();

        MockHttpServletResponse client1Get = getEvent();
        MockHttpServletResponse client2Get = getEvent();

        initialEtag = client1Get.getHeader(HttpHeaders.ETAG);
        assertNotNull(initialEtag, "GET " + eventUri + " returned no ETag header");
        org.junit.jupiter.api.Assertions.assertEquals(initialEtag, client2Get.getHeader(HttpHeaders.ETAG));
        initialBody = client1Get.getContentAsString();
        System.out.println("[optimistic-locking] initial ETag = " + initialEtag);
    }

    @When("client 1 updates the event address to {string}")
    public void clientUpdatesTheEventAddress(String address) throws Throwable {
        client1Result = patchAddress(address, initialEtag);
    }

    @Then("client 1 update succeeds")
    public void clientUpdateSucceeds() throws Throwable {
        client1Result.andExpect(status().is2xxSuccessful());
        System.out.println("[optimistic-locking] client 1 ETag after update = "
                + client1Result.andReturn().getResponse().getHeader(HttpHeaders.ETAG));
    }

    @When("client 2 updates the event address to {string} using the initial version")
    public void clientUpdatesTheEventAddressUsingTheInitialVersion(String address) throws Throwable {
        client2Result = patchAddress(address, initialEtag);
        stepDefs.result = client2Result;
    }

    @Then("client 2 update is rejected with status {int}")
    public void clientUpdateIsRejectedWithStatus(int code) throws Throwable {
        MockHttpServletResponse response = client2Result.andReturn().getResponse();
        System.out.println("[optimistic-locking] client 2 status = " + response.getStatus()
                + ", body = " + response.getContentAsString());
        client2Result.andExpect(status().is(code));
    }

    @Given("the event titled {string} is loaded twice from the repository")
    public void theEventIsLoadedTwiceFromTheRepository(String title) {
        entityA = loadEventTitled(title);
        entityB = loadEventTitled(title);
    }

    @When("the first copy of the event is changed to address {string} and saved")
    public void theFirstCopyIsChangedAndSaved(String address) {
        entityA.setAddress(address);
        eventRepository.save(entityA);
    }

    @When("the second copy of the event is changed to address {string} and saved")
    public void theSecondCopyIsChangedAndSaved(String address) {
        entityB.setAddress(address);
        try {
            eventRepository.save(entityB);
        } catch (RuntimeException e) {
            saveException = e;
        }
    }

    @Then("an optimistic locking exception is thrown")
    public void anOptimisticLockingExceptionIsThrown() {
        System.out.println("[optimistic-locking] repository save threw: " + saveException);
        assertInstanceOf(OptimisticLockingFailureException.class, saveException);
    }

    private Event loadEventTitled(String title) {
        return StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .filter(e -> title.equals(e.getTitle()))
                .findFirst()
                .orElseThrow();
    }

    private MockHttpServletResponse getEvent() throws Throwable {
        return stepDefs.mockMvc.perform(
                get(eventUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print())
            .andReturn().getResponse();
    }

    private ResultActions patchAddress(String address, String etag) throws Throwable {
        MockHttpServletRequestBuilder request = patch(eventUri);
        if (etag != null) {
            request.header(HttpHeaders.IF_MATCH, etag);
        }
        return stepDefs.mockMvc.perform(
                request
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(stepDefs.mapper.writeValueAsString(Map.of("address", address)))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }
}
