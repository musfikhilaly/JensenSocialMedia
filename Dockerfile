
# -------------------------
# Runtime image
# -------------------------
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy the JAR from the build stage
COPY target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
