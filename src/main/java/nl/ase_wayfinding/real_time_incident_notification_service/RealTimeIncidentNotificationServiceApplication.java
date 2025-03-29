package nl.ase_wayfinding.real_time_incident_notification_service;

import nl.ase_wayfinding.real_time_incident_notification_service.config.AwsSecretsInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "nl.ase_wayfinding.real_time_incident_notification_service.repositories")
public class RealTimeIncidentNotificationServiceApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(RealTimeIncidentNotificationServiceApplication.class);
		app.addInitializers(new AwsSecretsInitializer());
		app.run(args);
	}
}
