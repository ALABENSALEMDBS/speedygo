FROM eclipse-temurin:17-jdk-alpine
EXPOSE 9999
COPY target/APIGateway-0.0.1-SNAPSHOT.jar apigateway.jar
ENTRYPOINT ["java", "-jar", "apigateway.jar"]