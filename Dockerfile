# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build

# Instala net-tools para ter o comando netstat
RUN apt-get update && apt-get install -y net-tools && rm -rf /var/lib/apt/lists/*

WORKDIR /library
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /library
COPY --from=build /library/target/*.jar library.jar
EXPOSE 8080 5005
#ENTRYPOINT ["java", "-jar", "library.jar"]
CMD ["java", "-jar", "library.jar"]
