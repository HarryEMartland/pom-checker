FROM tomcat:8.5.6-jre8-alpine

ARG GIT_VERSION=local
ENV GIT_VERSION ${GIT_VERSION}

RUN rm -r /usr/local/tomcat/webapps/*

ADD ./target/pom-checker-${GIT_VERSION} /usr/local/tomcat/webapps/ROOT