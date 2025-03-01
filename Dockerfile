FROM maven:3.9.4-eclipse-temurin-17 AS build
COPY src src
COPY pom.xml pom.xml
RUN mvn clean package

FROM eclipse-temurin:23.0.2_7-jre-alpine-3.21

RUN adduser --system spring-boot && addgroup --system spring-boot && adduser spring-boot spring-boot
USER spring-boot

WORKDIR /app

COPY --from=build target/Paste-0.0.1-SNAPSHOT.jar ./pasteApp.jar
ENTRYPOINT ["java", "-jar", "pasteApp.jar"]