FROM eclipse-temurin:21-jdk

WORKDIR /app

CMD ["java", "-jar", "/app/app-0.0.1-SNAPSHOT.jar"]
