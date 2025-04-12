package nl.ase_wayfinding.real_time_incident_notification_service.services;

import nl.ase_wayfinding.real_time_incident_notification_service.models.IncidentModel;
import nl.ase_wayfinding.real_time_incident_notification_service.repositories.IncidentRepository;
import nl.ase_wayfinding.real_time_incident_notification_service.requests.IncidentRequest;
import nl.ase_wayfinding.real_time_incident_notification_service.responses.IncidentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncidentServiceTests {

    @Mock
    private IncidentRepository incidentRepository;

    @InjectMocks
    private IncidentService incidentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateIncident() {
        IncidentRequest request = new IncidentRequest();
        // Using reflection to set private fields on the request
        setField(request, "incidentType", "TestType");
        setField(request, "location", "TestLocation");
        setField(request, "lat", 10.0);
        setField(request, "lon", 20.0);
        setField(request, "description", "Test Description");
        // Corrected timestamp format
        String createdAtStr = "2025-04-12 10:10:10.000";
        setField(request, "createdAt", createdAtStr);

        // Create a dummy IncidentModel that would be returned by repository.save(...)
        IncidentModel incidentModel = request.toIncidentModel();
        UUID dummyId = UUID.randomUUID();
        incidentModel.setId(dummyId);

        when(incidentRepository.save(any(IncidentModel.class))).thenReturn(incidentModel);

        IncidentResponse response = incidentService.createIncident(request);

        // Verify that repository.save was called and the IncidentResponse contains the right data.
        ArgumentCaptor<IncidentModel> captor = ArgumentCaptor.forClass(IncidentModel.class);
        verify(incidentRepository, times(1)).save(captor.capture());
        IncidentModel captured = captor.getValue();

        assertEquals("TestType", captured.getIncidentType());
        assertEquals("TestLocation", captured.getLocation());
        assertEquals(10.0, captured.getLat());
        assertEquals(20.0, captured.getLon());
        assertEquals("Test Description", captured.getDescription());
        assertEquals(dummyId, response.getId());
    }

    @Test
    void testGetIncidentsRange() {
        String start = "2025-04-12 00:00:00.000";
        String end = "2025-04-12 23:59:59.000";
        Timestamp startTimestamp = Timestamp.valueOf(start);
        Timestamp endTimestamp = Timestamp.valueOf(end);

        IncidentModel model = new IncidentModel();
        model.setId(UUID.randomUUID());
        model.setIncidentType("TestType");
        model.setLocation("TestLocation");
        model.setLat(10.0);
        model.setLon(20.0);
        model.setDescription("Test Description");
        model.setCreatedAt(startTimestamp);

        when(incidentRepository.findAllByCreatedAtBetween(startTimestamp, endTimestamp))
                .thenReturn(Collections.singletonList(model));

        List<IncidentResponse> responses = incidentService.getIncidentsRange(start, end);

        assertEquals(1, responses.size());
        IncidentResponse res = responses.get(0);
        assertEquals(model.getId(), res.getId());
        assertEquals("TestType", res.getIncidentType());
    }

    // Utility method to set private fields via reflection.
    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
