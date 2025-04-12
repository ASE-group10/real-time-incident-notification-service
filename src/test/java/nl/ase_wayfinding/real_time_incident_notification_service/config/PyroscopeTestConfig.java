package nl.ase_wayfinding.real_time_incident_notification_service.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import nl.ase_wayfinding.real_time_incident_notification_service.beans.PyroscopeBean;

@TestConfiguration
public class PyroscopeTestConfig {

    /**
     * Creates a mock PyroscopeBean for testing that doesn't require the
     * pyroscope server properties, preventing test failures.
     */
    @Bean
    @Primary
    public PyroscopeBean pyroscopeBean() {
        // Return a mock implementation for testing
        return new PyroscopeBean();
    }
}
