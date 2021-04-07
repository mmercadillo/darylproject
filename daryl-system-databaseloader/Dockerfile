FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/databaseloader-1.0.4.jar databaseloader-1.0.4.jar
ENTRYPOINT ["java","-jar","/databaseloader-1.0.4.jar"]