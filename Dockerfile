FROM eclipse-temurin:17-alpine
ARG JAR_FILE=build/libs/connectme.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
