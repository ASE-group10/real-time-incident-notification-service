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

    private String activeProfile;
    private String applicationName;
    private String pyroscopeServerAddress;
    private String pyroscopeServerAuthUser;
    private String pyroscopeServerAuthPassword;

    // Default constructor for Spring to use with @Value
    public PyroscopeBean(
            @Value("${spring.profiles.active:default}") String activeProfile,
            @Value("${spring.application.name:app}") String applicationName,
            @Value("${pyroscope.server.address:}") String pyroscopeServerAddress,
            @Value("${pyroscope.auth.user:}") String pyroscopeServerAuthUser,
            @Value("${pyroscope.auth.password:}") String pyroscopeServerAuthPassword) {
        this.activeProfile = activeProfile;
        this.applicationName = applicationName;
        this.pyroscopeServerAddress = pyroscopeServerAddress;
        this.pyroscopeServerAuthUser = pyroscopeServerAuthUser;
        this.pyroscopeServerAuthPassword = pyroscopeServerAuthPassword;
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
        if (pyroscopeServerAuthUser.isEmpty() || pyroscopeServerAuthPassword.isEmpty()) {
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
