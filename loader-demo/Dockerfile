# ========= ETAPA 1: BUILD =========
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos todo el repo (porque es multi-módulo)
COPY . .

# Compilamos SOLO loader-demo y sus dependencias
RUN mvn -DskipTests -Dmaven.test.skip=true clean package -pl loader-demo -am


# ========= ETAPA 2: RUN =========
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el JAR generado
COPY --from=build /app/loader-demo/target/*.jar app.jar

EXPOSE 8700

# Arranque con perfil docker
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=docker"]
