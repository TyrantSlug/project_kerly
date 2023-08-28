FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/kerly_docker-1.0.jar KerlyDocker.jar
ENTRYPOINT ["java", "-jar", "KerlyDocker.jar"]