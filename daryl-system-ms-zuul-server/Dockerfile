FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8888
ADD ./target/zuulserver-1.0.0.jar zuulserver-1.0.0.jar
ENTRYPOINT ["java","-jar","/zuulserver-1.0.0.jar"]