package cat.udl.eps.softarch.demo.config;
import cat.udl.eps.softarch.demo.domain.Category;
import cat.udl.eps.softarch.demo.domain.Subcategory;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.CategoryRepository;
import cat.udl.eps.softarch.demo.repository.SubcategoryRepository;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

import java.util.Arrays;
import java.util.Map;

@Configuration
public class DBInitialization {
    @Value("${default-password}")
    String defaultPassword;

    @Value("${spring.profiles.active:}")
    private String activeProfiles;

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    public DBInitialization(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            SubcategoryRepository subcategoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @PostConstruct
    public void initializeDatabase() {
        initializeCategories();

        // Default user
        if (!userRepository.existsById("demo")) {
            User user = new User();
            user.setEmail("demo@sample.app");
            user.setId("demo");
            user.setFullName("Demo User");
            user.setPassword(defaultPassword);
            user.encodePassword();
            userRepository.save(user);
        }
        if (Arrays.asList(activeProfiles.split(",")).contains("test")) {
            // Testing instances
            if (!userRepository.existsById("test")) {
                User user = new User();
                user.setEmail("test@sample.app");
                user.setId("test");
                user.setFullName("Test User");
                user.setPassword(defaultPassword);
                user.encodePassword();
                userRepository.save(user);
            }
        }
    }

    private void initializeCategories() {
        Map<String, java.util.List<String>> categories = Map.of(
                "Sport", java.util.List.of("Running", "Hiking", "Football", "Basketball"),
                "Study", java.util.List.of("Lectures", "Exams", "Assignments", "Meetings"),
                "Party", java.util.List.of("Birthdays", "Nightlife", "House party", "Cultural events"),
                "Trip", java.util.List.of("Weekend getaway", "Erasmus trip", "Road trip", "Sightseeing")
        );

        categories.forEach((categoryName, subcategoryNames) -> {
            Category category = categoryRepository.findByName(categoryName).orElseGet(() -> {
                Category newCategory = new Category();
                newCategory.setName(categoryName);
                return categoryRepository.save(newCategory);
            });

            subcategoryNames.forEach(subcategoryName -> {
                if (!subcategoryRepository.existsByNameAndCategory_Name(subcategoryName, categoryName)) {
                    Subcategory subcategory = new Subcategory();
                    subcategory.setName(subcategoryName);
                    subcategory.setCategory(category);
                    subcategoryRepository.save(subcategory);
                }
            });
        });
    }
}
