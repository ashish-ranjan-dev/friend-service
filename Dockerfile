FROM openjdk:17
EXPOSE 8091
ADD ./build/libs/friendship-service-0.0.1-SNAPSHOT.jar friendship-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","friendship-service-0.0.1-SNAPSHOT.jar"]
