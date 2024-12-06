package nl.ase_wayfinding.real_time_incident_notification_service.services;

import nl.ase_wayfinding.real_time_incident_notification_service.requests.UserStoreRoutesRequest;
import nl.ase_wayfinding.real_time_incident_notification_service.responses.UserStoreRoutesResponse;
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

    public UserStoreRoutesResponse getRoutes(double latitude, double longitude, double radius) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserStoreRoutesRequest> body = new HttpEntity<>(new UserStoreRoutesRequest(latitude, longitude, radius), headers);

        ResponseEntity<UserStoreRoutesResponse> response = restTemplate.postForEntity(userStoreUrl + "/api/routes/near-incident", body, UserStoreRoutesResponse.class);

        return response.getBody();
    }

}
