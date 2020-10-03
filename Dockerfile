FROM maven:3.6.3-jdk-11-slim
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn clean package -DskipTests

FROM openjdk:11-jdk-slim
COPY --from=0 /workspace/target/*.jar libman-hex.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","libman-hex.jar", "--spring.profiles.active=prod"]
