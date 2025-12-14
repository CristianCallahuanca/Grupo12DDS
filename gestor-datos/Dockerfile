FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -DskipTests -Dmaven.test.skip=true clean package -pl gestor-datos -am

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/gestor-datos/target/*.jar app.jar
EXPOSE 8500
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=docker"]
