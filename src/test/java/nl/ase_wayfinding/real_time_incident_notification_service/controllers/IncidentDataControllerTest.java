package nl.ase_wayfinding.real_time_incident_notification_service.controllers;

import nl.ase_wayfinding.real_time_incident_notification_service.requests.IncidentRequest;
import nl.ase_wayfinding.real_time_incident_notification_service.responses.IncidentResponse;
import nl.ase_wayfinding.real_time_incident_notification_service.responses.UserStoreNearUsersResponse;
import nl.ase_wayfinding.real_time_incident_notification_service.services.IncidentService;
import nl.ase_wayfinding.real_time_incident_notification_service.services.SMSService;
import nl.ase_wayfinding.real_time_incident_notification_service.services.UserStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncidentDataControllerTest {

    @Mock
    private SMSService smsService;

    @Mock
    private IncidentService incidentService;

    @Mock
    private UserStoreService userStoreService;

    @InjectMocks
    private IncidentDataController incidentDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Utility to set private fields via reflection.
    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateIncidentWithoutSMS() {
        IncidentRequest incidentRequest = new IncidentRequest();
        setField(incidentRequest, "incidentType", "OtherType");
        setField(incidentRequest, "location", "Location1");
        setField(incidentRequest, "lat", 10.0);
        setField(incidentRequest, "lon", 20.0);
        setField(incidentRequest, "description", "Incident description");
        setField(incidentRequest, "createdAt", "2025-04-12 10:10:10.0000");

        UserStoreNearUsersResponse nearUsersResponse = new UserStoreNearUsersResponse();
        nearUsersResponse.setPhoneNumbers(Collections.emptyList());
        when(userStoreService.getPhoneNumbers(10.0, 20.0, 100)).thenReturn(nearUsersResponse);

        IncidentResponse incidentResponse = new IncidentResponse(new nl.ase_wayfinding.real_time_incident_notification_service.models.IncidentModel());
        setField(incidentResponse, "id", UUID.randomUUID());
        when(incidentService.createIncident(incidentRequest)).thenReturn(incidentResponse);

        ResponseEntity<IncidentResponse> responseEntity = incidentDataController.createIncident(incidentRequest);

        verify(userStoreService, times(1)).getPhoneNumbers(10.0, 20.0, 100);
        verify(incidentService, times(1)).createIncident(incidentRequest);
        verify(smsService, never()).sendSMSAsync(anyString(), anyString());

        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals(incidentResponse, responseEntity.getBody());
    }

    @Test
    void testCreateIncidentWithSMS() {
        IncidentRequest incidentRequest = new IncidentRequest();
        setField(incidentRequest, "incidentType", "SendSMS");
        setField(incidentRequest, "location", "Location2");
        setField(incidentRequest, "lat", 30.0);
        setField(incidentRequest, "lon", 40.0);
        setField(incidentRequest, "description", "Send SMS test");
        setField(incidentRequest, "createdAt", "2025-04-12 11:11:11.0000");

        UserStoreNearUsersResponse nearUsersResponse = new UserStoreNearUsersResponse();
        nearUsersResponse.setPhoneNumbers(List.of("+1234567890"));
        when(userStoreService.getPhoneNumbers(30.0, 40.0, 100)).thenReturn(nearUsersResponse);

        nl.ase_wayfinding.real_time_incident_notification_service.models.IncidentModel model =
                new nl.ase_wayfinding.real_time_incident_notification_service.models.IncidentModel();
        model.setId(UUID.randomUUID());
        IncidentResponse incidentResponse = new IncidentResponse(model);
        when(incidentService.createIncident(incidentRequest)).thenReturn(incidentResponse);

        ResponseEntity<IncidentResponse> responseEntity = incidentDataController.createIncident(incidentRequest);

        verify(userStoreService, times(1)).getPhoneNumbers(30.0, 40.0, 100);
        verify(incidentService, times(1)).createIncident(incidentRequest);
        verify(smsService, times(1)).sendSMSAsync("Send SMS test", "+1234567890");

        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals(incidentResponse, responseEntity.getBody());
    }

    @Test
    void testGetIncidents() {
        String start = "2025-04-12 00:00:00.000";
        String end = "2025-04-12 23:59:59.000";
        IncidentResponse response1 = new IncidentResponse(new nl.ase_wayfinding.real_time_incident_notification_service.models.IncidentModel());
        List<IncidentResponse> responseList = List.of(response1);
        when(incidentService.getIncidentsRange(start, end)).thenReturn(responseList);

        ResponseEntity<List<IncidentResponse>> responseEntity = incidentDataController.getIncidents(start, end);

        verify(incidentService, times(1)).getIncidentsRange(start, end);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(responseList, responseEntity.getBody());
    }
}
