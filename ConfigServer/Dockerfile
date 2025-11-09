FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8888
COPY target/ConfigServer-0.0.1-SNAPSHOT.jar confserver.jar
ENTRYPOINT ["java", "-jar", "confserver.jar"]