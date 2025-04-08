package nl.ase_wayfinding.real_time_incident_notification_service.controllers;

import nl.ase_wayfinding.real_time_incident_notification_service.requests.IncidentRequest;
import nl.ase_wayfinding.real_time_incident_notification_service.responses.IncidentResponse;
import nl.ase_wayfinding.real_time_incident_notification_service.responses.UserStoreNearUsersResponse;
import nl.ase_wayfinding.real_time_incident_notification_service.services.IncidentService;
import nl.ase_wayfinding.real_time_incident_notification_service.services.SMSService;
import nl.ase_wayfinding.real_time_incident_notification_service.services.UserStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
public class IncidentDataController {
    private final SMSService smsService;
    private final IncidentService incidentService;
    private final UserStoreService userStoreService;

    public IncidentDataController(SMSService smsService, IncidentService incidentService, UserStoreService userStoreService) {
        this.smsService = smsService;
        this.incidentService = incidentService;
        this.userStoreService = userStoreService;
    }

    @PostMapping("/v1/incidents")
    public ResponseEntity<IncidentResponse> createIncident(@RequestBody IncidentRequest incident) {
        System.out.println("sike");

        UserStoreNearUsersResponse phoneNumbers = userStoreService.getPhoneNumbers(incident.getLat(), incident.getLon(), 100);
        if (phoneNumbers.getPhoneNumbers().isEmpty()) {
            IncidentResponse response = incidentService.createIncident(incident);
            String uri = "/v1/incidents/" + response.getId();
            return ResponseEntity.created(URI.create(uri)).body(response);
        }

        IncidentResponse response = incidentService.createIncident(incident);
        String uri = "/v1/incidents/" + response.getId();

        if (Objects.equals(incident.getIncidentType(), "SendSMS")) {
            smsService.sendSMSAsync(incident.getDescription(), String.valueOf(phoneNumbers.getPhoneNumbers().get(0)));
        }

        return ResponseEntity.created(URI.create(uri)).body(response);
    }

    @GetMapping("/v1/incidents")
    public ResponseEntity<List<IncidentResponse>> getIncidents(@RequestParam String startDateTime, @RequestParam String endDateTime) {
        List<IncidentResponse> response = incidentService.getIncidentsRange(startDateTime, endDateTime);
        return ResponseEntity.ok().body(response);
    }
}
