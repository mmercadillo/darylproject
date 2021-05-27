FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/robot-arima-c-1.0.4.jar robot-arima-c-1.0.4.jar
ENTRYPOINT ["java","-jar","/robot-arima-c-1.0.4.jar"]