#Get base image from Docker hub
FROM openjdk:17-jdk-slim

MAINTAINER khanhduytran

COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]