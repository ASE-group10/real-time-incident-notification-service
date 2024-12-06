package nl.ase_wayfinding.real_time_incident_notification_service.responses;

import nl.ase_wayfinding.real_time_incident_notification_service.responses.route.Route;

import java.util.List;

public class UserStoreRoutesResponse {
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }
}

