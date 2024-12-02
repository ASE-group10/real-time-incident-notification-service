package nl.ase_wayfinding.real_time_incident_notification_service.services;

import nl.ase_wayfinding.real_time_incident_notification_service.models.IncidentModel;
import nl.ase_wayfinding.real_time_incident_notification_service.repositories.IncidentRepository;
import nl.ase_wayfinding.real_time_incident_notification_service.requests.IncidentRequest;
import nl.ase_wayfinding.real_time_incident_notification_service.responses.IncidentResponse;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public IncidentResponse createIncident(IncidentRequest incidentRequest) {
        return new IncidentResponse(incidentRepository.save(incidentRequest.toIncidentModel()));
    }

    public List<IncidentResponse> getIncidentsRange(String start, String end) {

        Timestamp startDateTime = Timestamp.valueOf(start);
        Timestamp endDateTime = Timestamp.valueOf(end);

        List<IncidentModel> incidentModelList = incidentRepository.findAllByCreatedAtBetween(startDateTime, endDateTime);

        return convertIncidentModelListToIncidentResponseList(incidentModelList);
    }

    private List<IncidentResponse> convertIncidentModelListToIncidentResponseList(List<IncidentModel> incidentModelList) {
        return incidentModelList.stream().map(IncidentResponse::new).toList();
    }
}
