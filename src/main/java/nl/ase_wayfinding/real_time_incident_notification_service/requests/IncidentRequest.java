package nl.ase_wayfinding.real_time_incident_notification_service.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.ase_wayfinding.real_time_incident_notification_service.models.IncidentModel;

public class IncidentRequest {
    @JsonProperty("incident_type")
    private String incidentType;
    private String location;
    private Double lat;
    @JsonProperty("long")
    private Double lon;
    private String description;
    @JsonProperty("created_at")
    private String createdAt;

    public String getIncidentType() {
        return incidentType;
    }

    public String getLocation() {
        return location;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public IncidentModel toIncidentModel() {
        IncidentModel incidentModel = new IncidentModel();
        incidentModel.setIncidentType(incidentType);
        incidentModel.setLocation(location);
        incidentModel.setLat(lat);
        incidentModel.setLon(lon);
        incidentModel.setDescription(description);
        incidentModel.setCreatedAtFromString(createdAt);
        return incidentModel;
    }
}
