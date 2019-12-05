FROM openjdk:12

VOLUME /tmp

ADD ./target/st-microservice-administration-0.0.1-SNAPSHOT.jar st-microservice-administration.jar

EXPOSE 8080

ENTRYPOINT java -jar /st-microservice-administration.jar