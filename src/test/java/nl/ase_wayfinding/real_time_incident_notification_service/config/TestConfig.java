package nl.ase_wayfinding.real_time_incident_notification_service.config;

import nl.ase_wayfinding.real_time_incident_notification_service.beans.PyroscopeBean;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Test configuration class for the environmental data service.
 * Provides mock beans and property values for testing without
 * requiring external dependencies like Pyroscope or OpenWeather API.
 */
@TestConfiguration
@Profile("test")
public class TestConfig {

    /**
     * Mock property values for testing.
     * These are automatically used instead of the real values in the test
     * environment.
     */
    @Bean
    @Primary
    @Profile("test")
    public RestTemplate restTemplate() {
        // Return a real RestTemplate that can be mocked in tests
        return new RestTemplate();
    }

    /**
     * Mock property values for testing.
     * These are automatically used instead of the real values in the test
     * environment.
     */
    @Bean
    @Primary
    @Profile("test")
    public PyroscopeBean pyroscopeBean() {
        return new MockPyroscopeBean();
    }

    /**
     * Mock implementation of PyroscopeBean for testing
     */
    public static class MockPyroscopeBean extends PyroscopeBean {
        public MockPyroscopeBean() {
            super("test", "test-app", "mock-address", "mock-user", "mock-password");
        }
    }
}