package cat.udl.eps.softarch.demo.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Value("${allowed-origins}")
    String[] allowedOrigins;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(HttpMethod.GET, "/identity").authenticated()
                .requestMatchers(HttpMethod.GET, "/users/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/users").anonymous()
<<<<<<< Updated upstream
                .requestMatchers(HttpMethod.POST, "/users/*").denyAll()
                .requestMatchers("/users/*/suspension").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/reports", "/reports/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/reports/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/reports/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/reports/**").hasRole("ADMIN")
=======
                .requestMatchers(HttpMethod.POST, "/users/**").denyAll()
                .requestMatchers(HttpMethod.PUT, "/users/**").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/users/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/users/**").authenticated()
>>>>>>> Stashed changes
                .requestMatchers(HttpMethod.POST, "/categories", "/subcategories").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/*").authenticated()     // Collection creation, e.g. POST /events
                .requestMatchers(HttpMethod.GET, "/records").permitAll()    // Allow listing public records
                .requestMatchers(HttpMethod.GET, "/records/*").permitAll()  // Allow retrieving public records
                .requestMatchers(HttpMethod.POST, "/records/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/records/**").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/records/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/records/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/events", "/events/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/events").authenticated()
                .requestMatchers(HttpMethod.PUT, "/events/**").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/events/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/events/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/categories", "/categories/**",
                        "/subcategories", "/subcategories/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/categories/**", "/subcategories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/categories/**", "/subcategories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/categories/**", "/subcategories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/comments", "/comments/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/comments").authenticated()
                .requestMatchers(HttpMethod.PUT, "/comments/**").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/comments/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/comments/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/*/*").authenticated()
                .requestMatchers(HttpMethod.POST, "/*/*").authenticated()
                .requestMatchers(HttpMethod.PUT, "/*/*").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/*/*").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/*/*").authenticated()
                .requestMatchers("/events/*/participants").authenticated()  // Join and leave events
                .anyRequest().permitAll())
            .csrf((csrf) -> csrf.disable())
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
            .httpBasic((httpBasic) -> httpBasic
                .realmName("demo")
                .authenticationEntryPoint(new ApiAuthenticationEntryPoint("demo")))
            .logout((logout) -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList(allowedOrigins));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
