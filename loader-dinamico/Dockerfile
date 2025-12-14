# ========= ETAPA 1: BUILD =========
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY . .

# Compila SOLO loader-dinamico y sus dependencias
RUN mvn -DskipTests -Dmaven.test.skip=true clean package -pl loader-dinamico -am


# ========= ETAPA 2: RUN =========
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/loader-dinamico/target/*.jar app.jar

# directorio para uploads dentro del contenedor
RUN mkdir -p /app/uploads-fuente-dinamica

EXPOSE 8102

ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=docker"]
