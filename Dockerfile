FROM anapsix/alpine-java:8_server-jre_unlimited
COPY target/tomee-runner.jar /usr/tomee-runner/
COPY target/lib              /usr/tomee-runner/lib
EXPOSE 8080
