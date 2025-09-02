FROM openjdk:17

COPY . /app

WORKDIR /app

RUN ./mvnw clean install -DskipTests

CMD ["java", "-jar","target/proyecto-0.0.1-SNAPSHOT.jar"]

