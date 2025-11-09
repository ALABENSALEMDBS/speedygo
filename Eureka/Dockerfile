FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8761
COPY target/Eureka-0.0.1-SNAPSHOT.jar eureka.jar
ENTRYPOINT ["java", "-jar", "eureka.jar"]