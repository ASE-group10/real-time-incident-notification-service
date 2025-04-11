package nl.ase_wayfinding.real_time_incident_notification_service.services;

import nl.ase_wayfinding.real_time_incident_notification_service.requests.UserStoreRoutesRequest;
import nl.ase_wayfinding.real_time_incident_notification_service.responses.UserStoreRoutesResponse;
import nl.ase_wayfinding.real_time_incident_notification_service.responses.route.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserStoreServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserStoreService userStoreService;

    private final String TEST_USER_STORE_URL = "http://test-user-store-service";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(userStoreService, "userStoreUrl", TEST_USER_STORE_URL);

        // We need to replace the RestTemplate created in the getRoutes method
        // since we can't mock the constructor directly
        ReflectionTestUtils.setField(userStoreService, "restTemplate", restTemplate);
    }

    @Test
    void testGetRoutes_Success() {
        // Arrange
        double latitude = 52.3676;
        double longitude = 4.9041;
        double radius = 1.0;

        UserStoreRoutesResponse mockResponse = new UserStoreRoutesResponse();
        List<Route> routes = new ArrayList<>();

        Route route1 = new Route();
        route1.setAuth0UserId("user1");
        route1.setStatus("active");

        Route route2 = new Route();
        route2.setAuth0UserId("user2");
        route2.setStatus("active");

        routes.add(route1);
        routes.add(route2);

        mockResponse.setRoutes(routes);

        ResponseEntity<UserStoreRoutesResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(
                eq(TEST_USER_STORE_URL + "/api/routes/near-incident"),
                any(HttpEntity.class),
                eq(UserStoreRoutesResponse.class)))
                .thenReturn(responseEntity);

        // Act
        UserStoreRoutesResponse result = userStoreService.getRoutes(latitude, longitude, radius);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getRoutes().size());
        assertEquals("user1", result.getRoutes().get(0).getAuth0UserId());
        assertEquals("user2", result.getRoutes().get(1).getAuth0UserId());

        verify(restTemplate, times(1)).postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(UserStoreRoutesResponse.class));
    }

    @Test
    void testGetRoutes_EmptyResponse() {
        // Arrange
        double latitude = 52.3676;
        double longitude = 4.9041;
        double radius = 1.0;

        UserStoreRoutesResponse mockResponse = new UserStoreRoutesResponse();
        mockResponse.setRoutes(Collections.emptyList());

        ResponseEntity<UserStoreRoutesResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(UserStoreRoutesResponse.class)))
                .thenReturn(responseEntity);

        // Act
        UserStoreRoutesResponse result = userStoreService.getRoutes(latitude, longitude, radius);

        // Assert
        assertNotNull(result);
        assertTrue(result.getRoutes().isEmpty());
    }

    @Test
    void testGetRoutes_NullResponse() {
        // Arrange
        double latitude = 52.3676;
        double longitude = 4.9041;
        double radius = 1.0;

        ResponseEntity<UserStoreRoutesResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(UserStoreRoutesResponse.class)))
                .thenReturn(responseEntity);

        // Act & Assert
        assertNull(userStoreService.getRoutes(latitude, longitude, radius));
    }

    @Test
    void testGetRoutes_ServiceUnavailable() {
        // Arrange
        double latitude = 52.3676;
        double longitude = 4.9041;
        double radius = 1.0;

        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(UserStoreRoutesResponse.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable"));

        // Act & Assert
        Exception exception = assertThrows(HttpServerErrorException.class, () -> {
            userStoreService.getRoutes(latitude, longitude, radius);
        });

        assertTrue(exception.getMessage().contains("Service Unavailable"));
    }

    @Test
    void testGetRoutes_ConnectionError() {
        // Arrange
        double latitude = 52.3676;
        double longitude = 4.9041;
        double radius = 1.0;

        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(UserStoreRoutesResponse.class)))
                .thenThrow(new RestClientException("Connection refused"));

        // Act & Assert
        Exception exception = assertThrows(RestClientException.class, () -> {
            userStoreService.getRoutes(latitude, longitude, radius);
        });

        assertTrue(exception.getMessage().contains("Connection refused"));
    }

    @Test
    void testGetRoutes_VerifyRequestData() {
        // Arrange
        double latitude = 52.3676;
        double longitude = 4.9041;
        double radius = 2.5;

        UserStoreRoutesResponse mockResponse = new UserStoreRoutesResponse();
        mockResponse.setRoutes(Collections.emptyList());

        ResponseEntity<UserStoreRoutesResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(
                anyString(),
                argThat(new ArgumentMatcher<HttpEntity<?>>() {
                    @Override
                    public boolean matches(HttpEntity<?> argument) {
                        if (argument.getBody() instanceof UserStoreRoutesRequest) {
                            UserStoreRoutesRequest request = (UserStoreRoutesRequest) argument.getBody();
                            return request.getLatitude() == latitude
                                    && request.getLongitude() == longitude
                                    && request.getRadius() == radius;
                        }
                        return false;
                    }
                }),
                eq(UserStoreRoutesResponse.class)))
                .thenReturn(responseEntity);

        // Act
        userStoreService.getRoutes(latitude, longitude, radius);

        // Assert - verify the correct request data is sent
        verify(restTemplate).postForEntity(
                eq(TEST_USER_STORE_URL + "/api/routes/near-incident"),
                any(HttpEntity.class),
                eq(UserStoreRoutesResponse.class));
    }

    @Test
    void testGetRoutes_ZeroRadius() {
        // Arrange
        double latitude = 52.3676;
        double longitude = 4.9041;
        double radius = 0.0;

        UserStoreRoutesResponse mockResponse = new UserStoreRoutesResponse();
        mockResponse.setRoutes(Collections.emptyList());

        ResponseEntity<UserStoreRoutesResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(
                anyString(),
                argThat(new ArgumentMatcher<HttpEntity<?>>() {
                    @Override
                    public boolean matches(HttpEntity<?> argument) {
                        if (argument.getBody() instanceof UserStoreRoutesRequest) {
                            UserStoreRoutesRequest request = (UserStoreRoutesRequest) argument.getBody();
                            return request.getRadius() == 0.0;
                        }
                        return false;
                    }
                }),
                eq(UserStoreRoutesResponse.class)))
                .thenReturn(responseEntity);

        // Act
        UserStoreRoutesResponse result = userStoreService.getRoutes(latitude, longitude, radius);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testGetRoutes_NegativeRadius() {
        // Arrange
        double latitude = 52.3676;
        double longitude = 4.9041;
        double radius = -1.0; // Negative radius

        UserStoreRoutesResponse mockResponse = new UserStoreRoutesResponse();
        mockResponse.setRoutes(Collections.emptyList());

        ResponseEntity<UserStoreRoutesResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(
                anyString(),
                argThat(new ArgumentMatcher<HttpEntity<?>>() {
                    @Override
                    public boolean matches(HttpEntity<?> argument) {
                        if (argument.getBody() instanceof UserStoreRoutesRequest) {
                            UserStoreRoutesRequest request = (UserStoreRoutesRequest) argument.getBody();
                            return request.getRadius() == radius;
                        }
                        return false;
                    }
                }),
                eq(UserStoreRoutesResponse.class)))
                .thenReturn(responseEntity);

        // Act
        UserStoreRoutesResponse result = userStoreService.getRoutes(latitude, longitude, radius);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testGetRoutes_ExtremeLargeRadius() {
        // Arrange
        double latitude = 52.3676;
        double longitude = 4.9041;
        double radius = 1000.0; // Very large radius

        UserStoreRoutesResponse mockResponse = new UserStoreRoutesResponse();
        List<Route> routes = new ArrayList<>();

        Route route1 = new Route();
        route1.setAuth0UserId("user1");

        Route route2 = new Route();
        route2.setAuth0UserId("user2");

        Route route3 = new Route();
        route3.setAuth0UserId("user3");

        routes.add(route1);
        routes.add(route2);
        routes.add(route3);

        mockResponse.setRoutes(routes);

        ResponseEntity<UserStoreRoutesResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(UserStoreRoutesResponse.class)))
                .thenReturn(responseEntity);

        // Act
        UserStoreRoutesResponse result = userStoreService.getRoutes(latitude, longitude, radius);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getRoutes().size());
    }

    @Test
    void testGetRoutes_ExtremeCoordinates() {
        // Arrange - Test with extreme coordinate values
        double latitude = 90.0; // North Pole
        double longitude = 180.0; // International Date Line
        double radius = 5.0;

        UserStoreRoutesResponse mockResponse = new UserStoreRoutesResponse();
        mockResponse.setRoutes(Collections.emptyList());

        ResponseEntity<UserStoreRoutesResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(
                anyString(),
                argThat(new ArgumentMatcher<HttpEntity<?>>() {
                    @Override
                    public boolean matches(HttpEntity<?> argument) {
                        if (argument.getBody() instanceof UserStoreRoutesRequest) {
                            UserStoreRoutesRequest request = (UserStoreRoutesRequest) argument.getBody();
                            return request.getLatitude() == 90.0 && request.getLongitude() == 180.0;
                        }
                        return false;
                    }
                }),
                eq(UserStoreRoutesResponse.class)))
                .thenReturn(responseEntity);

        // Act
        UserStoreRoutesResponse result = userStoreService.getRoutes(latitude, longitude, radius);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testGetRoutes_MalformedResponse() {
        // Arrange
        double latitude = 52.3676;
        double longitude = 4.9041;
        double radius = 1.0;

        // Simulate a response with null routes
        UserStoreRoutesResponse malformedResponse = new UserStoreRoutesResponse();
        // Don't set any routes - test null handling

        ResponseEntity<UserStoreRoutesResponse> responseEntity = new ResponseEntity<>(malformedResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(UserStoreRoutesResponse.class)))
                .thenReturn(responseEntity);

        // Act
        UserStoreRoutesResponse result = userStoreService.getRoutes(latitude, longitude, radius);

        // Assert
        assertNotNull(result);
        // The getRoutes() method should handle null
        assertNull(result.getRoutes());
    }
}