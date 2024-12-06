package nl.ase_wayfinding.real_time_incident_notification_service.responses.route;

public class Route {
    private String auth0UserId;
    private RouteDetails[] routeDetails;
    private RouteLocation source;
    private RouteLocation destination;
    private String status;

    public String getAuth0UserId() {
        return auth0UserId;
    }

    public void setAuth0UserId(String auth0UserId) {
        this.auth0UserId = auth0UserId;
    }

    public RouteDetails[] getRouteDetails() {
        return routeDetails;
    }

    public void setRouteDetails(RouteDetails[] routeDetails) {
        this.routeDetails = routeDetails;
    }

    public RouteLocation getSource() {
        return source;
    }

    public void setSource(RouteLocation source) {
        this.source = source;
    }

    public RouteLocation getDestination() {
        return destination;
    }

    public void setDestination(RouteLocation destination) {
        this.destination = destination;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
