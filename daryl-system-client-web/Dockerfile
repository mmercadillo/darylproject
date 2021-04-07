FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 9090
ADD ./target/control-cotizaciones-zeromq-1.0.4.jar control-cotizaciones-zeromq-1.0.4.jar
ENTRYPOINT ["java","-jar","/control-cotizaciones-zeromq-1.0.4.jar"]