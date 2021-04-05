FROM openjdk:8
VOLUME /tmp
EXPOSE 8888
ADD ./target/zuulserver-1.0.0.jar zuulserver-1.0.0.jar
ENTRYPOINT ["java","-jar","/zuulserver-1.0.0.jar"]