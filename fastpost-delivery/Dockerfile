FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8084
COPY target/fastpost-delivery-0.0.1-SNAPSHOT.jar fastpost.jar
ENTRYPOINT ["java", "-jar", "fastpost.jar"]