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
import java.util.Arrays;

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

        List<IncidentResponse> responses = incidentService.getIncidentsRange("2024-03-13 09:00:00",
                "2024-03-13 11:00:00");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(uuid, responses.get(0).getId());

        verify(incidentRepository, times(1)).findAllByCreatedAtBetween(start, end);
    }

    @Test
    void testGetIncidentsRange_NoResults() {
        // Arrange
        Timestamp start = Timestamp.valueOf("2024-03-13 09:00:00");
        Timestamp end = Timestamp.valueOf("2024-03-13 11:00:00");

        when(incidentRepository.findAllByCreatedAtBetween(start, end)).thenReturn(Collections.emptyList());

        // Act
        List<IncidentResponse> responses = incidentService.getIncidentsRange("2024-03-13 09:00:00",
                "2024-03-13 11:00:00");

        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(incidentRepository, times(1)).findAllByCreatedAtBetween(start, end);
    }

    @Test
    void testGetIncidentsRange_MultipleResults() {
        // Arrange
        Timestamp start = Timestamp.valueOf("2024-03-13 09:00:00");
        Timestamp end = Timestamp.valueOf("2024-03-13 11:00:00");

        IncidentModel incident1 = new IncidentModel();
        incident1.setId(UUID.randomUUID());
        incident1.setLat(40.7128);
        incident1.setLon(-74.0060);
        incident1.setDescription("Traffic accident");
        incident1.setCreatedAt(Timestamp.valueOf("2024-03-13 09:30:00"));

        IncidentModel incident2 = new IncidentModel();
        incident2.setId(UUID.randomUUID());
        incident2.setLat(34.0522);
        incident2.setLon(-118.2437);
        incident2.setDescription("Road closure");
        incident2.setCreatedAt(Timestamp.valueOf("2024-03-13 10:15:00"));

        IncidentModel incident3 = new IncidentModel();
        incident3.setId(UUID.randomUUID());
        incident3.setLat(51.5074);
        incident3.setLon(-0.1278);
        incident3.setDescription("Construction work");
        incident3.setCreatedAt(Timestamp.valueOf("2024-03-13 10:45:00"));

        List<IncidentModel> mockIncidentList = Arrays.asList(incident1, incident2, incident3);
        when(incidentRepository.findAllByCreatedAtBetween(start, end)).thenReturn(mockIncidentList);

        // Act
        List<IncidentResponse> responses = incidentService.getIncidentsRange("2024-03-13 09:00:00",
                "2024-03-13 11:00:00");

        // Assert
        assertNotNull(responses);
        assertEquals(3, responses.size());

        // Verify each incident's data is correctly mapped
        assertEquals(incident1.getId(), responses.get(0).getId());
        assertEquals(incident1.getDescription(), responses.get(0).getDescription());

        assertEquals(incident2.getId(), responses.get(1).getId());
        assertEquals(incident2.getLat(), responses.get(1).getLat());
        assertEquals(incident2.getLon(), responses.get(1).getLon());

        // Note: The timestamps are stored as strings in the response, so we need to
        // compare them properly
        assertEquals(String.valueOf(incident3.getCreatedAt()), responses.get(2).getCreatedAt());
    }

    @Test
    void testCreateIncident_WithSpecificData() {
        // Create a model that would be the result of conversion from request
        IncidentModel savedModel = new IncidentModel();
        UUID uuid = UUID.randomUUID();
        savedModel.setId(uuid);
        savedModel.setLat(37.7749);
        savedModel.setLon(-122.4194);
        savedModel.setDescription("Flooding on main street");
        savedModel.setIncidentType("WEATHER");
        savedModel.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // Mock the behavior of our request and repository
        when(mockIncidentRequest.toIncidentModel()).thenReturn(savedModel);
        when(incidentRepository.save(any(IncidentModel.class))).thenReturn(savedModel);

        // Act
        IncidentResponse response = incidentService.createIncident(mockIncidentRequest);

        // Assert
        assertNotNull(response);
        assertEquals(uuid, response.getId());
        assertEquals(37.7749, response.getLat());
        assertEquals(-122.4194, response.getLon());
        assertEquals("Flooding on main street", response.getDescription());
        assertEquals("WEATHER", response.getIncidentType());

        verify(incidentRepository, times(1)).save(any(IncidentModel.class));
    }

    @Test
    void testGetIncidentsRange_InvalidDateFormat() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            incidentService.getIncidentsRange("invalid-date-format", "2024-03-13 11:00:00");
        });

        // Check that the exception message contains text about timestamp format
        String exceptionMessage = exception.getMessage();
        assertTrue(exceptionMessage.contains("format") || exceptionMessage.contains("Format"),
                "Exception message should mention format issue but was: " + exceptionMessage);
    }

    @Test
    void testGetIncidentsRange_EndBeforeStart() {
        // Arrange
        Timestamp start = Timestamp.valueOf("2024-03-13 11:00:00");
        Timestamp end = Timestamp.valueOf("2024-03-13 09:00:00");

        // End is before start, but the repository should still be called with these
        // parameters
        when(incidentRepository.findAllByCreatedAtBetween(start, end)).thenReturn(Collections.emptyList());

        // Act
        List<IncidentResponse> responses = incidentService.getIncidentsRange("2024-03-13 11:00:00",
                "2024-03-13 09:00:00");

        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(incidentRepository, times(1)).findAllByCreatedAtBetween(start, end);
    }

    @Test
    void testCreateIncident_WithNullDescription() {
        // Create a model with null description
        IncidentModel modelWithNullDesc = new IncidentModel();
        UUID uuid = UUID.randomUUID();
        modelWithNullDesc.setId(uuid);
        modelWithNullDesc.setLat(37.7749);
        modelWithNullDesc.setLon(-122.4194);
        modelWithNullDesc.setDescription(null);
        modelWithNullDesc.setIncidentType("ACCIDENT");
        modelWithNullDesc.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        when(mockIncidentRequest.toIncidentModel()).thenReturn(modelWithNullDesc);
        when(incidentRepository.save(any(IncidentModel.class))).thenReturn(modelWithNullDesc);

        IncidentResponse response = incidentService.createIncident(mockIncidentRequest);

        assertNotNull(response);
        assertEquals(uuid, response.getId());
        assertNull(response.getDescription());
        assertEquals("ACCIDENT", response.getIncidentType());
        verify(incidentRepository, times(1)).save(any(IncidentModel.class));
    }

    @Test
    void testCreateIncident_WithMinimumData() {
        // Create a model with minimum required data
        IncidentModel minModel = new IncidentModel();
        UUID uuid = UUID.randomUUID();
        minModel.setId(uuid);
        // Only set required fields according to business logic
        minModel.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        when(mockIncidentRequest.toIncidentModel()).thenReturn(minModel);
        when(incidentRepository.save(any(IncidentModel.class))).thenReturn(minModel);

        IncidentResponse response = incidentService.createIncident(mockIncidentRequest);

        assertNotNull(response);
        assertEquals(uuid, response.getId());
        verify(incidentRepository, times(1)).save(any(IncidentModel.class));
    }

    @Test
    void testGetIncidentsRange_BoundaryDates() {
        // Test with dates that are exactly at the boundary
        Timestamp exactStart = Timestamp.valueOf("2024-03-13 00:00:00");
        Timestamp exactEnd = Timestamp.valueOf("2024-03-13 23:59:59");

        IncidentModel incident = new IncidentModel();
        incident.setId(UUID.randomUUID());
        incident.setCreatedAt(Timestamp.valueOf("2024-03-13 00:00:00")); // Exactly at start time

        List<IncidentModel> mockIncidentList = Collections.singletonList(incident);
        when(incidentRepository.findAllByCreatedAtBetween(exactStart, exactEnd)).thenReturn(mockIncidentList);

        List<IncidentResponse> responses = incidentService.getIncidentsRange("2024-03-13 00:00:00",
                "2024-03-13 23:59:59");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(incidentRepository, times(1)).findAllByCreatedAtBetween(exactStart, exactEnd);
    }

    @Test
    void testGetIncidentsRange_InvalidEndDateFormat() {
        // Test with valid start date but invalid end date
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            incidentService.getIncidentsRange("2024-03-13 09:00:00", "invalid-end-date");
        });

        String exceptionMessage = exception.getMessage();
        assertTrue(exceptionMessage.contains("format") || exceptionMessage.contains("Format"),
                "Exception message should mention format issue but was: " + exceptionMessage);
    }

    @Test
    void testGetIncidentsRange_SameStartAndEndDate() {
        // Test case where start and end date are exactly the same
        Timestamp sameTime = Timestamp.valueOf("2024-03-13 10:00:00");

        IncidentModel incident = new IncidentModel();
        incident.setId(UUID.randomUUID());
        incident.setCreatedAt(sameTime);

        List<IncidentModel> mockIncidentList = Collections.singletonList(incident);
        when(incidentRepository.findAllByCreatedAtBetween(sameTime, sameTime)).thenReturn(mockIncidentList);

        List<IncidentResponse> responses = incidentService.getIncidentsRange("2024-03-13 10:00:00",
                "2024-03-13 10:00:00");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(incidentRepository, times(1)).findAllByCreatedAtBetween(sameTime, sameTime);
    }
}
