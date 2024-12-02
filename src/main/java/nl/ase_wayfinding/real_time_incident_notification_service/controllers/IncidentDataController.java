package nl.ase_wayfinding.real_time_incident_notification_service.controllers;

import nl.ase_wayfinding.real_time_incident_notification_service.models.IncidentModel;
import nl.ase_wayfinding.real_time_incident_notification_service.requests.IncidentRequest;
import nl.ase_wayfinding.real_time_incident_notification_service.responses.IncidentResponse;
import nl.ase_wayfinding.real_time_incident_notification_service.services.IncidentService;
import nl.ase_wayfinding.real_time_incident_notification_service.services.SMSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
public class IncidentDataController {
    private final SMSService smsService;
    private final IncidentService incidentService;

    public IncidentDataController(SMSService smsService, IncidentService incidentService) {
        this.smsService = smsService;
        this.incidentService = incidentService;
    }

    @PostMapping("/v1/incidents")
    public ResponseEntity<IncidentResponse> createIncident(@RequestBody IncidentRequest incident) {

        if (Objects.equals(incident.getIncidentType(), "SendSMS")) {
            smsService.sendSMS(incident.getDescription());
        }

        IncidentResponse response = incidentService.createIncident(incident);
        String uri = "/v1/incidents/" + response.getId();

        return ResponseEntity.created(URI.create(uri)).body(response);
    }

    // Make a Get Incident endpoint that takes two dates as parameters and returns all incidents that occurred between those dates
    @GetMapping("/v1/incidents")
    public ResponseEntity<List<IncidentResponse>> getIncidents(@RequestParam String startDateTime, @RequestParam String endDateTime) {
        List<IncidentResponse> response = incidentService.getIncidentsRange(startDateTime, endDateTime);
        return ResponseEntity.ok().body(response);
    }
}
