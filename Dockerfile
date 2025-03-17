FROM openjdk:21 as build

WORKDIR /app
COPY pom.xml mvnw mvnw.cmd /app/
COPY .mvn /app/.mvn
RUN ./mvnw dependency:go-offline
COPY src /app/src
RUN ./mvnw clean package -DskipTests

FROM openjdk:21

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT  ["java", "-jar", "/app/app.jar"]
