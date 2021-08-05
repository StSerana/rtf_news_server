FROM openjdk:11
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} rtf.jar
ENTRYPOINT ["java","-jar","/rtf.jar"]