# ========= ETAPA 1: BUILD =========
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos todo el repo (porque es multi-módulo)
COPY . .

# Compilamos SOLO loader-demo y sus dependencias
RUN mvn -DskipTests -Dmaven.test.skip=true clean package -pl loader-demo -am

COPY agente-otel/opentelemetry-javaagent.jar /app/agent.jar

# ========= ETAPA 2: RUN =========
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el JAR generado
COPY --from=build /app/loader-demo/target/*.jar app.jar
COPY --from=build /app/agent.jar /app/agent.jar

EXPOSE 8700

ENV JAVA_TOOL_OPTIONS="-javaagent:/app/agent.jar" \
    OTEL_SERVICE_NAME="metamapa-demo" \
    OTEL_EXPORTER_OTLP_ENDPOINT="http://otel-collector:4317" \
    OTEL_EXPORTER_OTLP_PROTOCOL="grpc" \
    OTEL_METRICS_EXPORTER="none" \
    OTEL_LOGS_EXPORTER="none"

# Arranque con perfil docker
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=docker"]
