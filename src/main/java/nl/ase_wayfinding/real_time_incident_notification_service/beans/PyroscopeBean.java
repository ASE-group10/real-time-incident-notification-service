package nl.ase_wayfinding.real_time_incident_notification_service.beans;

import io.pyroscope.http.Format;
import io.pyroscope.javaagent.EventType;
import io.pyroscope.javaagent.PyroscopeAgent;
import io.pyroscope.javaagent.config.Config;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PyroscopeBean {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${pyroscope.server.address}")
    private String pyroscopeServerAddress;

    @Value("${pyroscope.auth.user}")
    private String pyroscopeServerAuthUser;

    @Value("${pyroscope.auth.password}")
    private String pyroscopeServerAuthPassword;

    public PyroscopeBean() {
        System.out.println("PyroscopeBean created");
    }

    @PostConstruct
    public void init() {
        // Skip initialization on Windows or in test environment
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win") || "test".equals(activeProfile)) {
            System.out.println("Pyroscope is disabled on Windows or in test environment");
            return;
        }

        // Skip if auth credentials are empty
        if (pyroscopeServerAuthUser == null || pyroscopeServerAuthUser.isEmpty() ||
                pyroscopeServerAuthPassword == null || pyroscopeServerAuthPassword.isEmpty()) {
            System.out.println("Pyroscope is disabled due to missing credentials");
            return;
        }

        try {
            PyroscopeAgent.start(
                    new Config.Builder()
                            .setApplicationName(applicationName + "-" + activeProfile)
                            .setProfilingEvent(EventType.ITIMER)
                            .setProfilingEvent(EventType.CPU)
                            .setFormat(Format.JFR)
                            .setServerAddress(pyroscopeServerAddress)
                            .setBasicAuthUser(pyroscopeServerAuthUser)
                            .setBasicAuthPassword(pyroscopeServerAuthPassword)
                            .setProfilingAlloc("512k")
                            .build());
        } catch (Exception e) {
            System.out.println("Failed to initialize Pyroscope agent: " + e.getMessage());
        }
    }
}
