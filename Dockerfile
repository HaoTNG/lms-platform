FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/lms-1.0.0.jar backend-lms.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend-lms.jar"]
