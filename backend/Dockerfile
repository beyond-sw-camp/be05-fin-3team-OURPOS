FROM openjdk:17

WORKDIR /home/ubuntu

ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar

ENV USE_PROFILE=dev

ENTRYPOINT ["sh", "-c", "java -Duser.timezone=Asia/Seoul -Dspring.profiles.active=${USE_PROFILE} -jar app.jar"]
