package edu.escuelaing.arsw.medigo.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BasicConfigTest {

    @Test
    void restTemplate_ShouldReturnBean() {
        RestConfig config = new RestConfig();
        assertNotNull(config.restTemplate());
    }

    @Test
    void objectMapper_ShouldReturnBean() {
        JacksonConfig config = new JacksonConfig();
        assertNotNull(config.objectMapper());
    }
}
