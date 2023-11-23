# Use an official Maven image as a base image
FROM maven:3.8.1-openjdk-11-slim AS build

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the pom.xml file to the container
COPY pom.xml .

# Download and install Maven dependencies
RUN mvn dependency:go-offline

# Copy the application source code to the container
COPY src ./src

# Build the application
RUN mvn package

# Use an OpenJDK runtime image as a base image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the JAR file from the build stage to the container
COPY --from=build /usr/src/app/target/Test-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Specify the command to run your application
CMD ["java", "-jar", "app.jar"]