FROM openjdk:8
VOLUME /tmp
ADD ./target/chartdata-1.0.0.jar chartdata-1.0.0.jar
ENTRYPOINT ["java","-jar","/chartdata-1.0.0.jar"]