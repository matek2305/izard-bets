FROM openjdk:8-jdk-alpine
MAINTAINER matek2305@gmail.com
ARG JAR_FILE
ADD ${JAR_FILE} izard-bets.jar
ENTRYPOINT [ "java", "-Dspring.profiles.active=docker", "-jar", "/izard-bets.jar" ]

