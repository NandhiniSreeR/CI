FROM openjdk:8-jdk-alpine

RUN addgroup -S spring && adduser -S springuser -G spring
USER springuser
WORKDIR /home/springuser

ARG JAR_FILE=build/libs/bookshop-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]