package nl.ase_wayfinding.real_time_incident_notification_service.services;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SMSServiceTest {

    private SMSService smsService;

    @BeforeEach
    public void setup() {
        smsService = new SMSService();
        // Manually inject the values normally provided by Spring @Value annotations.
        ReflectionTestUtils.setField(smsService, "twilioPhoneNumber", "+1234567890");
        ReflectionTestUtils.setField(smsService, "phoneNumber", "+0987654321");
    }

    @Test
    public void testSendSMS() {
        String testMessage = "Hello from test!";
        String targetPhoneNumber = "+1122334455";
        String fakeSid = "SMXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

        MessageCreator messageCreatorMock = mock(MessageCreator.class);
        Message fakeMessage = mock(Message.class);

        when(messageCreatorMock.create()).thenReturn(fakeMessage);
        when(fakeMessage.getSid()).thenReturn(fakeSid);

        try (MockedStatic<Message> messageMockedStatic = Mockito.mockStatic(Message.class)) {
            messageMockedStatic.when(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    any(String.class)
            )).thenReturn(messageCreatorMock);

            smsService.sendSMS(testMessage, targetPhoneNumber);

            // Verify that the static method was called with the expected parameters.
            messageMockedStatic.verify(() -> Message.creator(
                    new PhoneNumber(targetPhoneNumber),
                    new PhoneNumber("+1234567890"),
                    testMessage
            ), times(1));

            // Verify that the create() method was invoked.
            verify(messageCreatorMock, times(1)).create();
        }
    }
}
