# ============ STAGE 1 - BUILD ============
FROM maven:3.9-eclipse-temurin-17 AS build
# set working directory inside container
WORKDIR /app
# copy your pom.xml from host
COPY pom.xml .
# copy your src/ folder from host
COPY src ./src
# builds JAR file
RUN mvn clean package -DskipTests

# ============ STAGE 2 - RUN ============
# fresh new image, totally separate from Stage 1
FROM eclipse-temurin:17-jdk-alpine
# takes JAR from Stage 1
COPY --from=build /app/target/*.jar app.jar
# runs the JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]