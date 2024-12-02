package nl.ase_wayfinding.real_time_incident_notification_service.beans;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.util.Objects;

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
        if (Objects.equals(accountSID, "") || Objects.equals(authToken, "")) {
            System.out.println("TwilioBean: accountSID or authToken is null");
            return;
        }

        Twilio.init(accountSID, authToken);
    }
}
