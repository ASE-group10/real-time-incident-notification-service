package nl.ase_wayfinding.real_time_incident_notification_service.responses.route;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RouteLocationTest {

    @Test
    void testNameGetterAndSetter() {
        RouteLocation location = new RouteLocation();
        location.setName("Central Station");
        assertEquals("Central Station", location.getName());
    }

    @Test
    void testLatitudeGetterAndSetter() {
        RouteLocation location = new RouteLocation();
        location.setLatitude(52.3791);
        assertEquals(52.3791, location.getLatitude(), 0.0001);
    }

    @Test
    void testLongitudeGetterAndSetter() {
        RouteLocation location = new RouteLocation();
        location.setLongitude(4.8994);
        assertEquals(4.8994, location.getLongitude(), 0.0001);
    }
}
