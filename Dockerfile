# Use a base image with OpenJDK for running Java applications
FROM openjdk:21-slim-bullseye as build

# Set the working directory in the container
WORKDIR /app

# Copy the compiled application files into the container
COPY target/pfa-backend-0.0.1-SNAPSHOT.jar /app/pfa-backend-0.0.1-SNAPSHOT.jar


# Stage 2 - the production environment
FROM openjdk:21-slim-bullseye 

# Set the working directory in the container
WORKDIR /app

# Copy the compiled application files from the build stage into the new stage
COPY --from=build /app /app

# Expose port 8024
EXPOSE 8024

# Command to run the Spring Boot application
CMD ["java", "-jar", "pfa-backend-0.0.1-SNAPSHOT.jar"]
