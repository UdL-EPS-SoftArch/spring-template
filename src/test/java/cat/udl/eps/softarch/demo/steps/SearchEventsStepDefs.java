package cat.udl.eps.softarch.demo.steps;

import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class SearchEventsStepDefs {
    private final StepDefs stepDefs;

    public SearchEventsStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @When("I search public events for {string}")
    public void iSearchPublicEventsFor(String keyword) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/events/search/findByTitleContainingIgnoreCase?keyword={keyword}", keyword)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }

    @When("I search public events for {string} in category {string}")
    public void iSearchPublicEventsForInCategory(String keyword, String category) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/events/search/findByTitleContainingIgnoreCaseAndSubcategory_Category_NameIgnoreCase"
                        + "?keyword={keyword}&category={category}", keyword, category)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(AuthenticationStepDefs.authenticate()))
            .andDo(print());
    }
}
