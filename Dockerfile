FROM openjdk:12
#can also write latest of any version number
WORKDIR /
#root
ADD cookery-service/build/libs/cookery-service-all.jar cookery-service.jar
 #adds jar file to container on the root with the name school-service.jar
EXPOSE 9000
 #port
ENTRYPOINT ["java", "-jar", "cookery-service.jar"]
