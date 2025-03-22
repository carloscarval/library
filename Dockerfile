# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /library
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /library
COPY --from=build /library/target/*.jar library.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "library.jar"]