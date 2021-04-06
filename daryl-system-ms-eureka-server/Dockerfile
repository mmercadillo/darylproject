FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8761
ADD ./target/eurekaserver-1.0.0.jar eurekaserver-1.0.0.jar
ENTRYPOINT ["java","-jar","/eurekaserver-1.0.0.jar"]