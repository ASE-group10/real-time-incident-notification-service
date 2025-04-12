package nl.ase_wayfinding.real_time_incident_notification_service.beans;

import com.twilio.Twilio;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;

class TwilioBeanTest {

    // Utility to set private fields via reflection.
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testInitTwilio() {
        TwilioBean twilioBean = new TwilioBean();
        setPrivateField(twilioBean, "accountSID", "dummySID");
        setPrivateField(twilioBean, "authToken", "dummyToken");

        try (MockedStatic<Twilio> mockedTwilio = Mockito.mockStatic(Twilio.class)) {
            twilioBean.init();
            mockedTwilio.verify(() -> Twilio.init("dummySID", "dummyToken"), times(1));
        }
    }

    @Test
    void testInitTwilioEmptyCredentials() {
        TwilioBean twilioBean = new TwilioBean();
        setPrivateField(twilioBean, "accountSID", "");
        setPrivateField(twilioBean, "authToken", "");

        try (MockedStatic<Twilio> mockedTwilio = Mockito.mockStatic(Twilio.class)) {
            twilioBean.init();
            mockedTwilio.verifyNoInteractions();
        }
    }
}
