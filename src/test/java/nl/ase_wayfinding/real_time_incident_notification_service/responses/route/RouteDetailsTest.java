package nl.ase_wayfinding.real_time_incident_notification_service.responses.route;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RouteDetailsTest {

    @Test
    void testNameGetterAndSetter() {
        RouteDetails details = new RouteDetails();
        details.setName("Main Street");
        assertEquals("Main Street", details.getName());
    }

    @Test
    void testLatitudeGetterAndSetter() {
        RouteDetails details = new RouteDetails();
        details.setLatitude(52.3702);
        assertEquals(52.3702, details.getLatitude(), 0.0001);
    }

    @Test
    void testLongitudeGetterAndSetter() {
        RouteDetails details = new RouteDetails();
        details.setLongitude(4.8952);
        assertEquals(4.8952, details.getLongitude(), 0.0001);
    }
}
