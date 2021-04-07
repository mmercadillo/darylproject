FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/arima-c-calculator-close-1.0.4.jar arima-c-calculator-close-1.0.4.jar
ENTRYPOINT ["java","-jar","/arima-c-calculator-close-1.0.4.jar"]