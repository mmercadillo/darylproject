FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/top5-1.0.0.jar top5-1.0.0.jar
ENTRYPOINT ["java","-jar","/top5-1.0.0.jar"]