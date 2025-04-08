package nl.ase_wayfinding.real_time_incident_notification_service.services;

import nl.ase_wayfinding.real_time_incident_notification_service.requests.UserStoreNearUsersRequest;
import nl.ase_wayfinding.real_time_incident_notification_service.responses.UserStoreNearUsersResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserStoreService {

    @Value("${services.user-store.url}")
    private String userStoreUrl;

    public UserStoreNearUsersResponse getPhoneNumbers(double latitude, double longitude, int radius) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserStoreNearUsersRequest> body = new HttpEntity<>(new UserStoreNearUsersRequest(latitude, longitude, radius), headers);

        ResponseEntity<UserStoreNearUsersResponse> response = restTemplate.postForEntity(userStoreUrl + "/api/routes/nearby-users", body, UserStoreNearUsersResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            System.out.println("Error: " + response.getStatusCode());
            return null;
        }
    }
}
