package nl.ase_wayfinding.real_time_incident_notification_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import nl.ase_wayfinding.real_time_incident_notification_service.models.IncidentModel;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface IncidentRepository extends JpaRepository<IncidentModel, UUID> {
    List<IncidentModel> findAllByCreatedAtBetween(Timestamp startDateTime, Timestamp endDateTime);
}
