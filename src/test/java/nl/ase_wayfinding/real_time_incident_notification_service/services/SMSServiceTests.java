package nl.ase_wayfinding.real_time_incident_notification_service.services;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SMSServiceTests {

    @InjectMocks
    private SMSService smsService;

    @Mock
    private Message mockedMessage;

    @Mock
    private MessageCreator messageCreator;

    private final String testRecipientPhoneNumber = "+1234567890";
    private final String testTwilioPhoneNumber = "+0987654321";
    private final String testMessageText = "Test SMS message";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(smsService, "phoneNumber", testRecipientPhoneNumber);
        ReflectionTestUtils.setField(smsService, "twilioPhoneNumber", testTwilioPhoneNumber);
    }

    @Test
    void testSendSMS() {
        try (MockedStatic<Message> mockedStaticMessage = mockStatic(Message.class)) {
            mockedStaticMessage.when(() -> Message.creator(
                    new PhoneNumber(testRecipientPhoneNumber),
                    new PhoneNumber(testTwilioPhoneNumber),
                    testMessageText)).thenReturn(messageCreator);

            when(messageCreator.create()).thenReturn(mockedMessage);
            when(mockedMessage.getSid()).thenReturn("SM123456789");

            assertDoesNotThrow(() -> smsService.sendSMS(testMessageText, testRecipientPhoneNumber));

            mockedStaticMessage.verify(() -> Message.creator(
                    new PhoneNumber(testRecipientPhoneNumber),
                    new PhoneNumber(testTwilioPhoneNumber),
                    testMessageText));
            verify(messageCreator).create();
            verify(mockedMessage).getSid();
        }
    }

    @Test
    void testSendSMSAsync() throws InterruptedException {
        SMSService spyService = spy(smsService);

        spyService.sendSMSAsync(testMessageText, testRecipientPhoneNumber);

        Thread.sleep(100);

        verify(spyService).sendSMS(testMessageText, testRecipientPhoneNumber);
    }

    @Test
    void testSendSMSWithEmptyMessage() {
        try (MockedStatic<Message> mockedStaticMessage = mockStatic(Message.class)) {
            mockedStaticMessage.when(() -> Message.creator(
                    new PhoneNumber(testRecipientPhoneNumber),
                    new PhoneNumber(testTwilioPhoneNumber),
                    "")).thenReturn(messageCreator);

            when(messageCreator.create()).thenReturn(mockedMessage);
            when(mockedMessage.getSid()).thenReturn("SM123456789");

            assertDoesNotThrow(() -> smsService.sendSMS("", testRecipientPhoneNumber));
        }
    }

    @Test
    void testSendSMSWithLongMessage() {
        String longMessage = "This is a very long message that should exceed the standard SMS length limit of 160 characters. "
                +
                "Let's make sure our service can handle messages that might be split into multiple SMS segments by the Twilio API.";

        try (MockedStatic<Message> mockedStaticMessage = mockStatic(Message.class)) {
            mockedStaticMessage.when(() -> Message.creator(
                    new PhoneNumber(testRecipientPhoneNumber),
                    new PhoneNumber(testTwilioPhoneNumber),
                    longMessage)).thenReturn(messageCreator);

            when(messageCreator.create()).thenReturn(mockedMessage);
            when(mockedMessage.getSid()).thenReturn("SM123456789");

            assertDoesNotThrow(() -> smsService.sendSMS(longMessage, testRecipientPhoneNumber));
        }
    }

    @Test
    void testSendSMSWithSpecialCharacters() {
        String messageWithSpecialChars = "Special chars: áéíóú ñ €$¥£ 你好 😊 \n\t";

        try (MockedStatic<Message> mockedStaticMessage = mockStatic(Message.class)) {
            mockedStaticMessage.when(() -> Message.creator(
                    new PhoneNumber(testRecipientPhoneNumber),
                    new PhoneNumber(testTwilioPhoneNumber),
                    messageWithSpecialChars)).thenReturn(messageCreator);

            when(messageCreator.create()).thenReturn(mockedMessage);
            when(mockedMessage.getSid()).thenReturn("SM123456789");

            assertDoesNotThrow(() -> smsService.sendSMS(messageWithSpecialChars, testRecipientPhoneNumber));
        }
    }

    @Test
    void testSendSMSHandlesException() {
        try (MockedStatic<Message> mockedStaticMessage = mockStatic(Message.class)) {
            mockedStaticMessage.when(() -> Message.creator(
                    new PhoneNumber(testRecipientPhoneNumber),
                    new PhoneNumber(testTwilioPhoneNumber),
                    testMessageText)).thenReturn(messageCreator);

            when(messageCreator.create()).thenThrow(new RuntimeException("API Error"));

            // Should not throw exception to the caller
            assertDoesNotThrow(() -> smsService.sendSMS(testMessageText));

            mockedStaticMessage.verify(() -> Message.creator(
                    new PhoneNumber(testRecipientPhoneNumber),
                    new PhoneNumber(testTwilioPhoneNumber),
                    testMessageText));
            verify(messageCreator).create();
        }
    }

    @Test
    void testSendSMSAsyncHandlesException() throws InterruptedException {
        SMSService spyService = spy(smsService);
        doThrow(new RuntimeException("SMS sending failed")).when(spyService).sendSMS(testMessageText);

        // Should not throw exception even if the underlying sendSMS fails
        assertDoesNotThrow(() -> spyService.sendSMSAsync(testMessageText));

        Thread.sleep(100); // Wait for async operation
        verify(spyService).sendSMS(testMessageText);
    }
}