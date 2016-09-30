FROM openjdk:8-jre-alpine
COPY target/tomee-runner.jar /usr/local/tomee-runner/
COPY target/lib              /usr/local/tomee-runner/lib
EXPOSE 8080
WORKDIR /usr/local/tomee-runner
CMD exec java -jar tomee-runner.jar /usr/local/webapp/*.war
