package nl.ase_wayfinding.real_time_incident_notification_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import nl.ase_wayfinding.real_time_incident_notification_service.models.IncidentModel;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface IncidentRepository extends JpaRepository<IncidentModel, UUID> {
}
