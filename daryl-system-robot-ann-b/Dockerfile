FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/robot-rna-1.0.4.jar robot-rna-1.0.4.jar
ENTRYPOINT ["java","-jar","/robot-rna-1.0.4.jar"]