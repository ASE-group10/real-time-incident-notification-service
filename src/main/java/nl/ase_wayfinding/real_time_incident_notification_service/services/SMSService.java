package nl.ase_wayfinding.real_time_incident_notification_service.services;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMSService {

    @Value("${demo.phoneNumber}")
    private String phoneNumber;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

//    public void sendSMS(String phoneNumber, String message) {
    public void sendSMS(String message) {
        Message smsMessage = Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                message
        ).create();
        System.out.println("SMS sent: " + smsMessage.getSid());
    }
}