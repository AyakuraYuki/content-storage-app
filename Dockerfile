FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER ayakurayuki YukiAyakura@outlook.com

VOLUME /tmp

ENV WORK_DIR=/data/content_storage_app
ENV JAR_FILE=target/content_storage_app-2.1.0-alpha-2.jar

RUN mkdir -p $WORK_DIR
ADD $JAR_FILE /$WORK_DIR/csa.jar
ADD data/storage.db /$WORK_DIR/data/
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/$WORK_DIR/csa.jar"]