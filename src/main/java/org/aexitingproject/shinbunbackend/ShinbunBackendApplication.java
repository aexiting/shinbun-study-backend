package org.aexitingproject.shinbunbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ShinbunBackendApplication {
    public static void main(String[] args) {
        // Entry point for the Spring Boot application
        SpringApplication.run(ShinbunBackendApplication.class, args);
    }

    /**
     * Creates and configures a RestTemplate bean.
     * Spring will manage this bean and inject it where needed (e.g., in WeatherService).
     * @return A configured RestTemplate instance.
     */
    @Bean
    public RestTemplate restTemplate() {
        // Return a new instance of RestTemplate
        // Further configuration (e.g., setting timeouts, message converters) can be done here
        return new RestTemplate();
    }
}
