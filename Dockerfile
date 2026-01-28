FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy the built JAR from the Maven build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
