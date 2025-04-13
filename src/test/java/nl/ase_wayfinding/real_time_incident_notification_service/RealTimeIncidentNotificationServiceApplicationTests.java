package nl.ase_wayfinding.real_time_incident_notification_service;

import nl.ase_wayfinding.real_time_incident_notification_service.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class RealTimeIncidentNotificationServiceApplicationTests {

	@Test
	void contextLoads() {
		// Test will load application context with our mock PyroscopeBean
	}
}
