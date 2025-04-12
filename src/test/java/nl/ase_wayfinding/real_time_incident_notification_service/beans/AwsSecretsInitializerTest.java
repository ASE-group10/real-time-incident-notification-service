package nl.ase_wayfinding.real_time_incident_notification_service.config;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class AwsSecretsInitializerTest {
    @Test
    void testInitializeInjectsSecrets() throws Exception {
        // Set up a mock environment for a non-local profile
        ConfigurableEnvironment environment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(environment.getProperty("spring.profiles.active", "default")).thenReturn("dev");
        MutablePropertySources propertySources = new MutablePropertySources();
        Mockito.when(environment.getPropertySources()).thenReturn(propertySources);

        ConfigurableApplicationContext context = Mockito.mock(ConfigurableApplicationContext.class);
        Mockito.when(context.getEnvironment()).thenReturn(environment);

        // Fake secret JSON string with two keys.
        String fakeSecretJson = "{\"SPRING_DATASOURCE_URL\": \"jdbc:postgresql://localhost:5432/mydb\", \"SPRING_DATASOURCE_USERNAME\": \"user\"}";

        SecretsManagerClient fakeClient = Mockito.mock(SecretsManagerClient.class);
        GetSecretValueResponse fakeResponse = GetSecretValueResponse.builder().secretString(fakeSecretJson).build();
        Mockito.when(fakeClient.getSecretValue(any(GetSecretValueRequest.class))).thenReturn(fakeResponse);

        try (MockedStatic<SecretsManagerClient> mockedClient = Mockito.mockStatic(SecretsManagerClient.class)) {
            mockedClient.when(SecretsManagerClient::create).thenReturn(fakeClient);

            AwsSecretsInitializer initializer = new AwsSecretsInitializer();
            initializer.initialize(context);

            // Verify that the secrets were injected into a property source.
            boolean found = false;
            for (org.springframework.core.env.PropertySource<?> source : propertySources) {
                if (source.getName().equals("aws-secrets")) {
                    found = true;
                    assertEquals("jdbc:postgresql://localhost:5432/mydb", source.getProperty("SPRING_DATASOURCE_URL"));
                    break;
                }
            }
            assertTrue(found, "aws-secrets property source should be added");
        }
    }

    @Test
    void testInitializeLocalProfileSkipsInjection() {
        ConfigurableEnvironment environment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(environment.getProperty("spring.profiles.active", "default")).thenReturn("local");
        MutablePropertySources propertySources = new MutablePropertySources();
        Mockito.when(environment.getPropertySources()).thenReturn(propertySources);

        ConfigurableApplicationContext context = Mockito.mock(ConfigurableApplicationContext.class);
        Mockito.when(context.getEnvironment()).thenReturn(environment);

        AwsSecretsInitializer initializer = new AwsSecretsInitializer();
        // Initialization in local mode should skip secrets injection.
        initializer.initialize(context);

        assertFalse(propertySources.contains("aws-secrets"));
    }
}
