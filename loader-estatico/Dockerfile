# ========= ETAPA 1: BUILD =========
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY . .

# Compila SOLO loader-estatico y sus dependencias
RUN mvn -DskipTests -Dmaven.test.skip=true clean package -pl loader-estatico -am


# ========= ETAPA 2: RUN =========
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/loader-estatico/target/*.jar app.jar

EXPOSE 8101

ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=docker"]
