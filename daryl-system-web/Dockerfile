FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 9090
ADD ./target/web-1.0.4.jar web-1.0.4.jar
ENTRYPOINT ["java","-jar","/web-1.0.4.jar"]