FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/real-time-incident-notification-service-0.0.1-SNAPSHOT.jar /app/real-time-incident-notification-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "/app/real-time-incident-notification-service-0.0.1-SNAPSHOT.jar"]
