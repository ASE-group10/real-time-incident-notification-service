package nl.ase_wayfinding.real_time_incident_notification_service.responses;

import nl.ase_wayfinding.real_time_incident_notification_service.models.IncidentModel;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncidentResponseTest {

    @Test
    void testGettersAndSetters() {
        IncidentResponse response = new IncidentResponse(mock(IncidentModel.class));

        UUID id = UUID.randomUUID();
        String type = "Accident";
        String location = "Amsterdam";
        Double lat = 52.3702;
        Double lon = 4.8952;
        String description = "Crash on main road";
        String createdAt = "2025-04-13T12:00:00Z";

        response.setId(id);
        response.setIncidentType(type);
        response.setLocation(location);
        response.setLat(lat);
        response.setLon(lon);
        response.setDescription(description);
        response.setCreatedAt(createdAt);

        assertEquals(id, response.getId());
        assertEquals(type, response.getIncidentType());
        assertEquals(location, response.getLocation());
        assertEquals(lat, response.getLat());
        assertEquals(lon, response.getLon());
        assertEquals(description, response.getDescription());
        assertEquals(createdAt, response.getCreatedAt());
    }

    @Test
    void testConstructorFromIncidentModel() {
        UUID id = UUID.randomUUID();
        String type = "Fire";
        String location = "Rotterdam";
        Double lat = 51.9225;
        Double lon = 4.47917;
        String description = "Warehouse fire";
        Instant createdAt = Instant.parse("2025-04-13T08:45:00Z");

        IncidentModel mockModel = mock(IncidentModel.class);
        when(mockModel.getId()).thenReturn(id);
        when(mockModel.getIncidentType()).thenReturn(type);
        when(mockModel.getLocation()).thenReturn(location);
        when(mockModel.getLat()).thenReturn(lat);
        when(mockModel.getLon()).thenReturn(lon);
        when(mockModel.getDescription()).thenReturn(description);
        when(mockModel.getCreatedAt()).thenReturn(Timestamp.from(createdAt));

        IncidentResponse response = new IncidentResponse(mockModel);

        assertEquals(id, response.getId());
        assertEquals(type, response.getIncidentType());
        assertEquals(location, response.getLocation());
        assertEquals(lat, response.getLat());
        assertEquals(lon, response.getLon());
        assertEquals(description, response.getDescription());
        assertEquals(createdAt.toString(), Timestamp.valueOf(response.getCreatedAt()).toInstant().toString());
    }
}
