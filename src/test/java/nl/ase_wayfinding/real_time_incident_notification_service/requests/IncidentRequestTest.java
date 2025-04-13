package nl.ase_wayfinding.real_time_incident_notification_service.requests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IncidentRequestTest {

    @Test
    void testGetLocation() {
        IncidentRequest request = new IncidentRequest();

        // Use reflection to set the private field 'location'
        try {
            var field = IncidentRequest.class.getDeclaredField("location");
            field.setAccessible(true);
            field.set(request, "Amsterdam");

            assertEquals("Amsterdam", request.getLocation());
        } catch (Exception e) {
            fail("Failed to set or access 'location' field: " + e.getMessage());
        }
    }

    @Test
    void testGetCreatedAt() {
        IncidentRequest request = new IncidentRequest();

        // Use reflection to set the private field 'createdAt'
        try {
            var field = IncidentRequest.class.getDeclaredField("createdAt");
            field.setAccessible(true);
            field.set(request, "2025-04-13T10:15:30Z");

            assertEquals("2025-04-13T10:15:30Z", request.getCreatedAt());
        } catch (Exception e) {
            fail("Failed to set or access 'createdAt' field: " + e.getMessage());
        }
    }
}
