FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /project

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk

WORKDIR /app

ARG JAR_NAME=spacecrafts-mariano.jar

COPY --from=builder /project/target/*.jar /app/${JAR_NAME}

EXPOSE 1986

CMD ["java", "-jar", "/app/spacecrafts-mariano.jar"]