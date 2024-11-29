package nl.ase_wayfinding.real_time_incident_notification_service.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "incidents")
public class IncidentModel {
    private UUID id;
    private String incidentType;
    private String location;
    private Double lat;
    private Double lon;
    private String description;
    private Timestamp createdAt;

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    @Column(unique = true)
    public UUID getId() {
        return id;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedAtFromString(String createdAt) {
        this.createdAt = Timestamp.valueOf(createdAt.substring(0, createdAt.length() - 4));
    }
}
