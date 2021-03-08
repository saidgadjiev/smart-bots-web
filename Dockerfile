FROM openjdk:11-jre

WORKDIR /web
COPY ./target/app.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-Duser.timezone=UTC", "-Duser.language=en", "-jar", "app.jar"]
