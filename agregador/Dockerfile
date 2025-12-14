# ========= ETAPA 1: BUILD =========
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos TODO el repo (porque es multi-módulo)
COPY . .

# Compilamos solo el módulo agregador y sus dependencias
RUN mvn -DskipTests -Dmaven.test.skip=true clean package -pl agregador -am

# ========= ETAPA 2: RUN =========
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el jar generado
COPY --from=build /app/agregador/target/*.jar app.jar

EXPOSE 8200
ENTRYPOINT ["java","-jar","app.jar"]
