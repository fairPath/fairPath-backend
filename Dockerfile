# ---------- Build stage ----------
FROM amazoncorretto:17-alpine AS build
WORKDIR /app

# Copy everything needed to build
COPY . .

# Build a runnable Spring Boot jar (skip tests for faster local builds)
RUN ./gradlew clean build -x test

# ---------- Runtime stage ----------
FROM amazoncorretto:17-alpine
WORKDIR /app

# Copy the built jar from the build stage (no hardcoded jar name)
COPY --from=build /app/build/libs/*.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
