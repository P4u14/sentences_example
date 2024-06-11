FROM eclipse-temurin:21

WORKDIR /app

COPY .mvn/ /app/.mvn
COPY mvnw pom.xml /app/
RUN ./mvnw dependency:go-offline

COPY src /app/src

CMD ["./mvnw", "spring-boot:run"]
