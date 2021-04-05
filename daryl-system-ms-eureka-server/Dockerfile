FROM openjdk:8
VOLUME /tmp
EXPOSE 8761
ADD ./target/eurekaserver-1.0.0.jar eurekaserver-1.0.0.jar
ENTRYPOINT ["java","-jar","/eurekaserver-1.0.0.jar"]