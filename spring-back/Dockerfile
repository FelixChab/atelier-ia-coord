# Use OpenJDK 17 as the base image
FROM eclipse-temurin:17

# Set working directory
WORKDIR /app

# Add a volume pointing to /tmp
VOLUME /tmp

# Copy the JAR file
COPY /target/*.jar app.jar

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV OPENAI_API_KEY=${OPENAI_API_KEY}

# Expose the port the app runs on
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]