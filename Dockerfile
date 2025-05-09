FROM amazoncorretto:17-alpine-jdk
LABEL maintainer="Weiwei Shi wshi27@hotmail.com"
COPY build/libs/fair_path-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]