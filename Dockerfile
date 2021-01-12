# Use Maven to build the project
FROM maven:3.6.3-openjdk-15 AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
RUN mvn install -f /tmp/pom.xml

# Copy target jar and generate image
FROM openjdk:15.0.1-jdk
ARG JAR_FILE=target/*.jar
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/*.jar gameServer.jar
ENTRYPOINT ["java","-jar","/gameServer.jar"]
