package nl.ase_wayfinding.real_time_incident_notification_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "nl.ase_wayfinding.real_time_incident_notification_service.repositories")
public class RealTimeIncidentNotificationServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RealTimeIncidentNotificationServiceApplication.class, args);
	}
}
