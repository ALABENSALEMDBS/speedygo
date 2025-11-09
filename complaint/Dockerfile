FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8086
COPY target/complaint-0.0.1-SNAPSHOT.jar complaint.jar
ENTRYPOINT ["java", "-jar", "complaint.jar"]