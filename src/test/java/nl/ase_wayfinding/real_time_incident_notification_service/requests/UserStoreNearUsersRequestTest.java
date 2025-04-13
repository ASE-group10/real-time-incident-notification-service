package nl.ase_wayfinding.real_time_incident_notification_service.requests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserStoreNearUsersRequestTest {

    @Test
    void testLatitudeGetterAndSetter() {
        UserStoreNearUsersRequest request = new UserStoreNearUsersRequest(0.0, 0.0, 0);
        request.setLatitude(52.3676);
        assertEquals(52.3676, request.getLatitude(), 0.0001);
    }

    @Test
    void testLongitudeGetterAndSetter() {
        UserStoreNearUsersRequest request = new UserStoreNearUsersRequest(0.0, 0.0, 0);
        request.setLongitude(4.9041);
        assertEquals(4.9041, request.getLongitude(), 0.0001);
    }

    @Test
    void testRadiusGetterAndSetter() {
        UserStoreNearUsersRequest request = new UserStoreNearUsersRequest(0.0, 0.0, 0);
        request.setRadius(1000);
        assertEquals(1000, request.getRadius());
    }
}
