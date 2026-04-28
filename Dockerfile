# ============ STAGE 1 - BUILD ============
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app                          # set working directory inside container

COPY pom.xml .                        # copy your pom.xml from host → /app/pom.xml in container
COPY src ./src                        # copy your src/ folder from host → /app/src in container

RUN mvn clean package -DskipTests     # builds JAR → creates /app/target/notifyhub-0.0.1.jar


# ============ STAGE 2 - RUN ============
FROM eclipse-temurin:17-jdk-alpine    # fresh new image, totally separate from Stage 1

COPY --from=build /app/target/*.jar app.jar
# ↑ takes JAR from Stage 1's /app/target/ → places at /app.jar in Stage 2

ENTRYPOINT ["java", "-jar", "/app.jar"]   # runs the JAR