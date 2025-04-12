package nl.ase_wayfinding.real_time_incident_notification_service.services;

import nl.ase_wayfinding.real_time_incident_notification_service.responses.UserStoreNearUsersResponse;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserStoreServiceTest {

    // Utility method to set private fields via reflection.
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetPhoneNumbersSuccess() {
        UserStoreService userStoreService = new UserStoreService();
        setPrivateField(userStoreService, "userStoreUrl", "http://dummy");

        UserStoreNearUsersResponse expectedResponse = new UserStoreNearUsersResponse();
        expectedResponse.setPhoneNumbers(java.util.List.of("+1234567890"));

        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    when(mock.postForEntity(anyString(), any(), eq(UserStoreNearUsersResponse.class)))
                            .thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));
                })) {

            UserStoreNearUsersResponse response = userStoreService.getPhoneNumbers(10.0, 20.0, 100);
            assertNotNull(response);
            assertEquals(1, response.getPhoneNumbers().size());
            assertEquals("+1234567890", response.getPhoneNumbers().get(0));
        }
    }

    @Test
    void testGetPhoneNumbersFailure() {
        UserStoreService userStoreService = new UserStoreService();
        setPrivateField(userStoreService, "userStoreUrl", "http://dummy");

        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    when(mock.postForEntity(anyString(), any(), eq(UserStoreNearUsersResponse.class)))
                            .thenReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
                })) {

            UserStoreNearUsersResponse response = userStoreService.getPhoneNumbers(10.0, 20.0, 100);
            assertNull(response);
        }
    }
}
