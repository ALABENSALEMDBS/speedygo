FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8083
COPY target/paiment-0.0.1-SNAPSHOT.jar paiment.jar
ENTRYPOINT ["java", "-jar", "paiment.jar"]