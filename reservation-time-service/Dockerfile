FROM openjdk:17-jdk-slim

WORKDIR /app

RUN mkdir -p /app/csv_files

COPY target/reservation-time-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]