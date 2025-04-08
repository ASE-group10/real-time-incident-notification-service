package nl.ase_wayfinding.real_time_incident_notification_service.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class UserStoreNearUsersResponse {
    @JsonProperty("phoneNumbers")
    private List<String> phoneNumbers;

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}