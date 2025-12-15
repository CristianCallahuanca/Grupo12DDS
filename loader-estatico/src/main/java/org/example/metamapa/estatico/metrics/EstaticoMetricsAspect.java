package org.example.metamapa.estatico.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class EstaticoMetricsAspect {

    private final MeterRegistry registry;

    // Cache para performance
    private final Map<String, Timer> timerCache = new ConcurrentHashMap<>();
    private final Map<String, Counter> counterCache = new ConcurrentHashMap<>();

    // ==================== MÉTRICAS ESPECÍFICAS DE CSV ====================
    private final Counter csvFilesProcessed;
    private final Counter csvRowsProcessed;
    private final Counter csvParsingErrors;
    private final Counter csvValidationErrors;

    public EstaticoMetricsAspect(MeterRegistry registry) {
        this.registry = registry;

        // Inicializar métricas ESPECÍFICAS de CSV
        this.csvFilesProcessed = Counter.builder("csv.files.processed")
                .description("Total de archivos CSV procesados")
                .tag("servicio", "loader-estatico")
                .register(registry);

        this.csvRowsProcessed = Counter.builder("csv.rows.processed")
                .description("Total de filas CSV procesadas")
                .tag("servicio", "loader-estatico")
                .register(registry);

        this.csvParsingErrors = Counter.builder("csv.parsing.errors")
                .description("Errores al parsear CSV")
                .tag("servicio", "loader-estatico")
                .register(registry);

        this.csvValidationErrors = Counter.builder("csv.validation.errors")
                .description("Errores de validación en CSV")
                .tag("servicio", "loader-estatico")
                .register(registry);

        // Métricas adicionales específicas
        Counter.builder("csv.columns.processed")
                .description("Columnas procesadas en CSV")
                .tag("servicio", "loader-estatico")
                .register(registry);
    }

    // ==================== INTERCEPTORES GENERALES ====================

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object aroundController(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "http.requests", "controller");
    }

    @Around("within(@org.springframework.stereotype.Service *)")
    public Object aroundService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "service.calls", "service");
    }

    @Around("within(@org.springframework.stereotype.Repository *)")
    public Object aroundRepository(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "database.queries", "repository");
    }

    // ==================== INTERCEPTORES ESPECÍFICOS DE CSV ====================

    @Around("execution(* *..*Csv*Service.*(..)) || execution(* *..*CSV*Service.*(..))")
    public Object aroundCsvServiceMethods(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "csv.service.operations", "csv_service");
    }

    @Around("execution(* *..*Csv*.*(..)) || execution(* *..*CSV*.*(..))")
    public Object aroundAnyCsvMethod(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "csv.operations", "csv");
    }

    @Around("execution(* *..*File*.*(..)) || execution(* *..*Archivo*.*(..))")
    public Object aroundFileMethods(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "file.operations", "file");
    }

    // ==================== MÉTRICAS DE NEGOCIO ESPECÍFICAS ====================

    /**
     * Contar archivos CSV procesados
     */
    @AfterReturning(
            pointcut = "execution(* *..*Csv*Service.procesar*(..)) || " +
                    "execution(* *..*CSV*Service.procesar*(..))",
            returning = "result"
    )
    public void afterProcesarCsv(JoinPoint jp, Object result) {
        csvFilesProcessed.increment();

        if (result instanceof List<?> list) {
            csvRowsProcessed.increment(list.size());

            // Métrica adicional: hechos creados desde CSV
            if (!list.isEmpty()) {
                String className = list.get(0).getClass().getSimpleName();
                if (className.contains("Hecho") || className.contains("DTO")) {
                    registry.counter("csv.hechos.creados",
                                    "servicio", "loader-estatico",
                                    "tipo", "csv")
                            .increment(list.size());
                }
            }
        }
    }

    /**
     * Medir tamaño de archivos CSV
     */
    @AfterReturning(
            pointcut = "execution(* *..*Csv*Service.obtenerTamanoArchivo(..))",
            returning = "tamano"
    )
    public void recordFileSize(JoinPoint jp, Long tamano) {
        if (tamano != null) {
            registry.summary("csv.file.size.bytes",
                            "servicio", "loader-estatico")
                    .record(tamano);
        }
    }

    /**
     * Contar columnas procesadas en CSV
     */
    @AfterReturning(
            pointcut = "execution(* *..*Csv*Service.procesarColumnas(..))",
            returning = "columnCount"
    )
    public void recordColumnsProcessed(JoinPoint jp, Integer columnCount) {
        if (columnCount != null) {
            registry.counter("csv.columns.processed",
                            "servicio", "loader-estatico",
                            "count", String.valueOf(columnCount))
                    .increment();
        }
    }

    /**
     * Manejar errores específicos de CSV
     */
    @AfterThrowing(
            pointcut = "execution(* *..*Csv*.*(..)) || execution(* *..*CSV*.*(..))",
            throwing = "ex"
    )
    public void handleCsvErrors(JoinPoint jp, Exception ex) {
        String exceptionName = ex.getClass().getSimpleName();

        if (exceptionName.contains("Parse") || exceptionName.contains("Csv") ||
                exceptionName.contains("Format") || exceptionName.contains("Malformed")) {

            csvParsingErrors.increment();

            registry.counter("csv.errors.detailed",
                            "exception", exceptionName,
                            "servicio", "loader-estatico",
                            "metodo", getMethodName(jp))
                    .increment();
        }

        if (exceptionName.contains("Valid") || exceptionName.contains("Constraint")) {
            csvValidationErrors.increment();
        }
    }

    /**
     * Métricas para validaciones de datos
     */
    @AfterReturning(
            pointcut = "execution(* *..*Validator.*(..)) || execution(* *..*ValidationService.*(..))",
            returning = "result"
    )
    public void afterValidation(JoinPoint jp, Object result) {
        if (result instanceof Boolean valid && valid) {
            registry.counter("csv.validations.passed",
                            "servicio", "loader-estatico")
                    .increment();
        } else if (result instanceof Integer errorCount) {
            registry.counter("csv.validation.errors.count",
                            "servicio", "loader-estatico",
                            "count", String.valueOf(errorCount))
                    .increment(errorCount);
        }
    }

    // ==================== MÉTODO PRINCIPAL DE REGISTRO ====================

    private Object recordExecution(ProceedingJoinPoint pjp, String metricName, String kind) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String className = sig.getDeclaringType().getSimpleName();
        String method = sig.getName();

        Timer.Sample sample = Timer.start(registry);
        try {
            Object result = pjp.proceed();

            // Registrar timer de éxito
            sample.stop(getOrCreateTimer(metricName + ".seconds",
                    "kind", kind,
                    "servicio", "loader-estatico",
                    "class", className,
                    "method", method,
                    "result", "success"));

            // Registrar contador de éxitos
            incrementCounter(metricName + ".count",
                    "kind", kind,
                    "servicio", "loader-estatico",
                    "class", className,
                    "method", method,
                    "result", "success");

            return result;

        } catch (Throwable t) {
            // Registrar timer de error
            sample.stop(getOrCreateTimer(metricName + ".seconds",
                    "kind", kind,
                    "servicio", "loader-estatico",
                    "class", className,
                    "method", method,
                    "result", "error"));

            // Registrar contador de errores
            String exceptionName = t.getClass().getSimpleName();
            incrementCounter(metricName + ".errors",
                    "exception", exceptionName,
                    "kind", kind,
                    "servicio", "loader-estatico",
                    "class", className,
                    "method", method);

            // Métricas específicas para errores críticos
            if (exceptionName.contains("IO") || exceptionName.contains("FileNotFound")) {
                incrementCounter("csv.errors.critical",
                        "type", "file_system",
                        "servicio", "loader-estatico");
            }

            throw t;
        }
    }

    // ==================== UTILIDADES ====================

    private Timer getOrCreateTimer(String name, String... tags) {
        String key = name + String.join("", tags);
        return timerCache.computeIfAbsent(key, k ->
                Timer.builder(name)
                        .tags(tags)
                        .register(registry));
    }

    private void incrementCounter(String name, String... tags) {
        String key = name + String.join("", tags);
        Counter counter = counterCache.computeIfAbsent(key, k ->
                Counter.builder(name)
                        .tags(tags)
                        .register(registry));
        counter.increment();
    }

    private String getMethodName(JoinPoint jp) {
        return jp.getSignature().getName();
    }
}