FROM azul/zulu-openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["nohup","java","-jar","/app.jar","&"]