FROM openjdk:17

WORKDIR /home/ec2-user
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} backend.jar

ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar

ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=${USE_PROFILE} -jar app.jar"]