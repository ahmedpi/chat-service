# Use a Java 21 base image
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy the built jar into the container
COPY target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]