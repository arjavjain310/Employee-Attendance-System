# Build stage
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

ENV PORT=8080

COPY --from=build /app/target/employee-attendance-system-1.0.0.jar app.jar

EXPOSE 8080

CMD sh -c "java -Dserver.port=$PORT -jar app.jar"
