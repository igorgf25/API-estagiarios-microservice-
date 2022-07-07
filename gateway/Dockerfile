FROM openjdk:11
WORKDIR /usr/src/myapp
COPY target/*.jar /app.jar
EXPOSE 8080
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-jar", "/app.jar"]