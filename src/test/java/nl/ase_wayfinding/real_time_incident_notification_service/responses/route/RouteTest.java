package nl.ase_wayfinding.real_time_incident_notification_service.responses.route;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    @Test
    void testAuth0UserIdGetterAndSetter() {
        Route route = new Route();
        route.setAuth0UserId("auth0|user123");
        assertEquals("auth0|user123", route.getAuth0UserId());
    }

    @Test
    void testStatusGetterAndSetter() {
        Route route = new Route();
        route.setStatus("IN_PROGRESS");
        assertEquals("IN_PROGRESS", route.getStatus());
    }

    @Test
    void testRouteDetailsGetterAndSetter() {
        Route route = new Route();
        RouteDetails detail1 = new RouteDetails(); // assume RouteDetails has a no-arg constructor
        RouteDetails detail2 = new RouteDetails();
        RouteDetails[] detailsArray = {detail1, detail2};

        route.setRouteDetails(detailsArray);
        assertArrayEquals(detailsArray, route.getRouteDetails());
    }

    @Test
    void testSourceGetterAndSetter() {
        Route route = new Route();
        RouteLocation source = new RouteLocation(); // assume RouteLocation has a no-arg constructor
        route.setSource(source);
        assertEquals(source, route.getSource());
    }

    @Test
    void testDestinationGetterAndSetter() {
        Route route = new Route();
        RouteLocation destination = new RouteLocation();
        route.setDestination(destination);
        assertEquals(destination, route.getDestination());
    }
}
