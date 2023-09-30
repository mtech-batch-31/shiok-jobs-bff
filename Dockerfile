FROM openjdk:17-alpine

COPY build/libs/sj_bff.jar /sj_bff.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/sj_bff.jar"]
