FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp

ENV WORK_DIR /opt/package/content_storage_app
ENV JAR_FILE ${WORK_DIR}/target/content_storage_app-2.1.0-alpha-1.jar

RUN mkdir -p ${WORK_DIR}
ADD ${JAR_FILE} /${WORK_DIR}/csa.jar
#COPY ${JAR_FILE} csa.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/${WORK_DIR}/csa.jar"]