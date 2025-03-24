package nl.ase_wayfinding.real_time_incident_notification_service.services;

import nl.ase_wayfinding.real_time_incident_notification_service.models.IncidentModel;
import nl.ase_wayfinding.real_time_incident_notification_service.repositories.IncidentRepository;
import nl.ase_wayfinding.real_time_incident_notification_service.requests.IncidentRequest;
import nl.ase_wayfinding.real_time_incident_notification_service.responses.IncidentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IncidentServiceTests {
    @Mock
    private IncidentRepository incidentRepository;

    @InjectMocks
    private IncidentService incidentService;

    private IncidentModel mockIncidentModel;
    private IncidentRequest mockIncidentRequest;

    @BeforeEach
    void setUp() {
        mockIncidentModel = new IncidentModel();
        UUID uuid = UUID.randomUUID();
        mockIncidentModel.setId(uuid);
        mockIncidentModel.setCreatedAt(Timestamp.valueOf("2024-03-13 10:00:00"));

        mockIncidentRequest = mock(IncidentRequest.class);
        lenient().when(mockIncidentRequest.toIncidentModel()).thenReturn(mockIncidentModel);
    }

    @Test
    void testCreateIncident() {
        UUID uuid = UUID.randomUUID();
        mockIncidentModel.setId(uuid);
        when(incidentRepository.save(any(IncidentModel.class))).thenReturn(mockIncidentModel);

        IncidentResponse response = incidentService.createIncident(mockIncidentRequest);

        assertNotNull(response);
        assertEquals(uuid, response.getId());
        verify(incidentRepository, times(1)).save(mockIncidentModel);
    }

    @Test
    void testGetIncidentsRange() {
        Timestamp start = Timestamp.valueOf("2024-03-13 09:00:00");
        Timestamp end = Timestamp.valueOf("2024-03-13 11:00:00");

        UUID uuid = UUID.randomUUID();
        mockIncidentModel.setId(uuid);

        List<IncidentModel> mockIncidentList = Collections.singletonList(mockIncidentModel);
        when(incidentRepository.findAllByCreatedAtBetween(start, end)).thenReturn(mockIncidentList);

        List<IncidentResponse> responses = incidentService.getIncidentsRange("2024-03-13 09:00:00", "2024-03-13 11:00:00");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(uuid, responses.get(0).getId());

        verify(incidentRepository, times(1)).findAllByCreatedAtBetween(start, end);
    }
}
