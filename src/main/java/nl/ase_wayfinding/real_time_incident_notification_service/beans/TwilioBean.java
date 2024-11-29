package nl.ase_wayfinding.real_time_incident_notification_service.beans;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioBean {

    @Value("${twilio.accountSID}")
    private String accountSID;

    @Value("${twilio.authToken}")
    private String authToken;

    public TwilioBean() {
        System.out.println("TwilioBean created");
    }

    @PostConstruct
    public void init() {
        System.out.println("TwilioBean init");
        Twilio.init(accountSID, authToken);
    }
}
