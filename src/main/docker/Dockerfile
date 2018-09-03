FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} csa.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/csa.jar"]