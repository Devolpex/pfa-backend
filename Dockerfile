FROM openjdk:21
ADD target/user-backend.jar user-backend.jar
ENTRYPOINT [ "java","-jar","/user-backend.jar" ]