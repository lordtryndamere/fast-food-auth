FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar authorization-server-0.0.1.jar
ENTRYPOINT ["java","-jar","/authorization-server-0.0.1.jar"]