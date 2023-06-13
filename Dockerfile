FROM openjdk:19
WORKDIR /app
COPY target/backend-0.1.0.jar /app/backend-0.1.0.jar
EXPOSE 8080
ENV DB_LOGIN=admin \
    DB_PASSWORD=123456 \
    DB_URL=postgres:5432/postgres \
    MAIL_USER=java.team.maksym@gmail.com \
    MAIL_PASSWORD=SoftServe123!
CMD ["java", "-jar", "backend-0.1.0.jar"]
