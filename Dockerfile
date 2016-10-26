FROM tomcat:8.5.6-jre8-alpine

RUN rm -r /usr/local/tomcat/webapps/*

ADD ./target/pom-checker-LATEST-DEV /usr/local/tomcat/webapps/ROOT