FROM azul/zulu-openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","-Djasypt_encryptor_password=${PW}","/app.jar"]
