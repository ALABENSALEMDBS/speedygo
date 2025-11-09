FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8087
COPY target/Covoiturage-0.0.1-SNAPSHOT.jar convoiturage.jar
ENTRYPOINT ["java", "-jar", "convoiturage.jar"]