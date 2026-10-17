package cat.udl.eps.softarch.demo;

import cat.udl.eps.softarch.demo.repository.CategoryRepository;
import cat.udl.eps.softarch.demo.repository.CommentRepository;
import cat.udl.eps.softarch.demo.repository.EventRepository;
import cat.udl.eps.softarch.demo.repository.SubcategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SubcategoryRepository subcategoryRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@BeforeEach
	void setUpMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void initialCategoriesAndSubcategoriesAreCreated() {
		assertThat(categoryRepository.count()).isEqualTo(4);
		assertThat(subcategoryRepository.count()).isEqualTo(16);
		assertThat(subcategoryRepository.findByNameAndCategory_Name("Running", "Sport"))
				.isPresent();
	}

	@Test
	void authenticatedUserCanCreateEventAndBecomesItsCreator() throws Exception {
		Long runningId = subcategoryRepository.findByNameAndCategory_Name("Running", "Sport")
				.orElseThrow()
				.getId();
		String location = mockMvc.perform(post("/events")
						.with(httpBasic("demo", "password"))
						.contentType(MediaType.APPLICATION_JSON)
						.content(eventRequestBody()))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getHeader("Location");

		assertThat(location).isNotNull();
		Long eventId = Long.valueOf(location.substring(location.lastIndexOf('/') + 1));
		assertThat(eventRepository.findAnyById(eventId))
				.hasValueSatisfying(event -> {
					assertThat(event.getCreator().getId()).isEqualTo("demo");
					assertThat(event.getSubcategory().getId()).isEqualTo(runningId);
				});
	}

	@Test
	void anonymousVisitorCannotCreateEvent() throws Exception {
		mockMvc.perform(post("/events")
						.contentType(MediaType.APPLICATION_JSON)
						.content(eventRequestBody()))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void categoriesArePubliclyReadableButOnlyAdminsCanCreateThem() throws Exception {
		mockMvc.perform(get("/categories"))
				.andExpect(status().isOk());

		mockMvc.perform(post("/categories")
						.with(httpBasic("demo", "password"))
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Other\"}"))
				.andExpect(status().isForbidden());
	}

	@Test
	void authenticatedUserCanCommentAndBecomesItsAuthor() throws Exception {
		String eventLocation = mockMvc.perform(post("/events")
						.with(httpBasic("demo", "password"))
						.contentType(MediaType.APPLICATION_JSON)
						.content(eventRequestBody()))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getHeader("Location");
		assertThat(eventLocation).isNotNull();

		String commentLocation = mockMvc.perform(post("/comments")
						.with(httpBasic("demo", "password"))
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "content": "Looking forward to it!",
								  "event": "%s"
								}
								""".formatted(eventLocation)))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getHeader("Location");

		assertThat(commentLocation).isNotNull();
		Long commentId = Long.valueOf(commentLocation.substring(commentLocation.lastIndexOf('/') + 1));
		assertThat(commentRepository.findById(commentId))
				.hasValueSatisfying(comment -> {
					assertThat(comment.getAuthor().getId()).isEqualTo("demo");
					assertThat(comment.getEvent().getId()).isNotNull();
					assertThat(comment.getCreatedAt()).isNotNull();
				});
	}

	private String eventRequestBody() {
		Long runningId = subcategoryRepository.findByNameAndCategory_Name("Running", "Sport")
				.orElseThrow()
				.getId();

		return """
				{
				  "title": "Welcome picnic",
				  "description": "Meet other Erasmus students",
				  "address": "City park",
				  "dateTime": "2030-11-10T16:00:00",
				  "maxParticipants": 20,
				  "estimatedCost": 5.00,
				  "subcategory": "http://localhost/subcategories/%d"
				}
				""".formatted(runningId);
	}
}
