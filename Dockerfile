FROM openjdk:8-jdk-alpine

COPY build/libs/vertx-fat.jar /opt/app/app.jar

EXPOSE 8080

WORKDIR /opt/app
ENTRYPOINT [ "java", "-jar", "app.jar" ]
