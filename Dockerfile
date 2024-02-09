# Build the app using Maven
FROM maven:3.8.5-openjdk-17 as build
COPY pom.xml /build/
COPY src /build/src
WORKDIR /build/
RUN mvn --batch-mode --fail-fast package

# Run the application (using the JRE, not the JDK)
# This assumes that your dependencies are packaged in app.jar
FROM eclipse-temurin:17-jre-alpine as runtime
ARG JAR_FILE=/build/target/*.jar
COPY --from=build ${JAR_FILE} ./app.jar  
EXPOSE 8080
ENTRYPOINT ["java","-jar","./app.jar"]