package nl.ase_wayfinding.real_time_incident_notification_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import nl.ase_wayfinding.real_time_incident_notification_service.config.PyroscopeTestConfig;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = PyroscopeTestConfig.class)
class RealTimeIncidentNotificationServiceApplicationTests {

	@Test
	void contextLoads() {
		// Test will load application context with our mock PyroscopeBean
	}
}
