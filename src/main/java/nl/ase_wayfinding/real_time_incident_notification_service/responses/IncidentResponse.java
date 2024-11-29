package nl.ase_wayfinding.real_time_incident_notification_service.responses;

import nl.ase_wayfinding.real_time_incident_notification_service.models.IncidentModel;

import java.util.UUID;

public class IncidentResponse {
    private UUID id;
    private String incidentType;
    private String location;
    private Double lat;
    private Double lon;
    private String description;
    private String createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public IncidentResponse(IncidentModel incidentModel) {
        this.id = incidentModel.getId();
        this.incidentType = incidentModel.getIncidentType();
        this.location = incidentModel.getLocation();
        this.lat = incidentModel.getLat();
        this.lon = incidentModel.getLon();
        this.description = incidentModel.getDescription();
        this.createdAt = String.valueOf(incidentModel.getCreatedAt());
    }
}
