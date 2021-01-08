#can also write latest of any version number
FROM openjdk:latest
#root
WORKDIR /

#adds jar file to container on the root with the name school-service.jar
ADD cookery-service/build/libs/cookery-service-1.0-SNAPSHOT-all.jar cookery-service.jar

#port
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "cookery-service.jar"]