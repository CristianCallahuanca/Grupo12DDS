FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -DskipTests -Dmaven.test.skip=true clean package -pl estadisticas -am

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/estadisticas/target/*.jar app.jar
EXPOSE 8600
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=docker"]
