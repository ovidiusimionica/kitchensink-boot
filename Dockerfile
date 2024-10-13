# First Stage: Build the application using Gradle
FROM eclipse-temurin:21-jdk AS builder

# Set the working directory inside the container
WORKDIR /app
# Copy Gradle wrapper and necessary files first to cache dependencies
COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY repos.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .

# Copy the rest of the project files

COPY config config
COPY app app
COPY member-registration member-registration


# Make the Gradle wrapper executable and build the project
RUN ./gradlew build --no-daemon -x test

# Second Stage: Run the application using a lightweight image with JDK 21
FROM eclipse-temurin:21-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the build stage to this stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]