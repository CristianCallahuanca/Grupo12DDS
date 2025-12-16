# ========= ETAPA 1: BUILD =========
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY . .

# Compila SOLO loader-estatico y sus dependencias
RUN mvn -DskipTests -Dmaven.test.skip=true clean package -pl loader-estatico -am

COPY agente-otel/opentelemetry-javaagent.jar /app/agent.jar

# ========= ETAPA 2: RUN =========
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/loader-estatico/target/*.jar app.jar
COPY --from=build /app/agent.jar /app/agent.jar

EXPOSE 8101

ENV JAVA_TOOL_OPTIONS="-javaagent:/app/agent.jar" \
    OTEL_SERVICE_NAME="metamapa-estatico" \
    OTEL_EXPORTER_OTLP_ENDPOINT="http://otel-collector:4317" \
    OTEL_EXPORTER_OTLP_PROTOCOL="grpc" \
    OTEL_METRICS_EXPORTER="none" \
    OTEL_LOGS_EXPORTER="none"

ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=docker"]
