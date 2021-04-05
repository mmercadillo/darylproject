FROM openjdk:8
VOLUME /tmp
EXPOSE 9090
ADD ./target/client-web-1.0.0.jar client-web-1.0.0.jar
ENTRYPOINT ["java","-jar","/client-web-1.0.0.jar"]