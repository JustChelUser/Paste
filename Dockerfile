FROM eclipse-temurin:23.0.2_7-jre-alpine-3.21
WORKDIR /app
COPY target/Paste-0.0.1-SNAPSHOT.jar Paste-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "Paste-0.0.1-SNAPSHOT.jar"]