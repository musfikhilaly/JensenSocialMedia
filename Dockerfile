# Step 1: Use Maven + JDK to build the project
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

# Copy your project files to Docker
COPY pom.xml .
COPY src ./src

# Build the JAR (skip tests if you want faster build)
RUN mvn clean package -DskipTests

# Step 2: Use a smaller JDK image to run the JAR
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy the built JAR from the Maven build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
