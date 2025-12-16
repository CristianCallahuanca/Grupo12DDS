# ========= ETAPA 1: BUILD =========
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos TODO el repo (porque es multi-módulo)
COPY . .

# Compilamos solo el módulo agregador y sus dependencias
RUN mvn -DskipTests -Dmaven.test.skip=true clean package -pl agregador -am

# TRAZAS OTel
COPY agente-otel/opentelemetry-javaagent.jar /app/agent.jar

# ========= ETAPA 2: RUN =========
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el jar generado
COPY --from=build /app/agregador/target/*.jar app.jar

COPY --from=build /app/agent.jar /app/agent.jar

EXPOSE 8200

# TRAZAS OTel (Inyectar el agente y las variables de configuración)
ENV JAVA_TOOL_OPTIONS="-javaagent:/app/agent.jar" \
    OTEL_SERVICE_NAME="metamapa-agregador" \
    OTEL_EXPORTER_OTLP_ENDPOINT="http://otel-collector:4317" \
    OTEL_EXPORTER_OTLP_PROTOCOL="grpc" \
    OTEL_METRICS_EXPORTER="none" \
    OTEL_LOGS_EXPORTER="none"

ENTRYPOINT ["java","--add-opens=java.base/java.util=ALL-UNNAMED","-jar","app.jar"]
