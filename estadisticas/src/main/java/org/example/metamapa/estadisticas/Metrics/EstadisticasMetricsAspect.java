package org.example.metamapa.estadisticas.Metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Aspecto completo de métricas para el módulo estadísticas.
 * Incluye métricas de generación, consulta y exportación de estadísticas.
 */
@Aspect
@Component
public class EstadisticasMetricsAspect {

    private final MeterRegistry registry;

    // Cache para performance
    private final Map<String, Timer> timerCache = new ConcurrentHashMap<>();
    private final Map<String, Counter> counterCache = new ConcurrentHashMap<>();

    // ==================== MÉTRICAS ESPECÍFICAS DEL MÓDULO ESTADÍSTICAS ====================
    private final Counter estadisticasGeneradasTotal;
    private final Counter consultasEstadisticasRealizadas;
    private final Counter csvExportadosTotal;
    private final Counter queriesSQLEjecutadas;
    private final Counter estadisticasProgramadasEjecutadas;

    // Gauges específicos
    private final AtomicInteger estadisticasEnCache;
    private final AtomicInteger categoriasMasReportadas;
    private final AtomicInteger solicitudesSpamDetectadas;

    public EstadisticasMetricsAspect(MeterRegistry registry) {
        this.registry = registry;

        // Inicializar métricas ESPECÍFICAS del módulo estadísticas
        this.estadisticasGeneradasTotal = Counter.builder("estadisticas.generadas")
                .description("Total de estadísticas generadas")
                .tag("servicio", "estadisticas")
                .register(registry);

        this.consultasEstadisticasRealizadas = Counter.builder("estadisticas.consultas.realizadas")
                .description("Consultas de estadísticas realizadas")
                .tag("servicio", "estadisticas")
                .register(registry);

        this.csvExportadosTotal = Counter.builder("estadisticas.csv.exportados")
                .description("Archivos CSV de estadísticas exportados")
                .tag("servicio", "estadisticas")
                .tag("formato", "csv")
                .register(registry);

        this.queriesSQLEjecutadas = Counter.builder("estadisticas.queries.sql.ejecutadas")
                .description("Consultas SQL ejecutadas para generar estadísticas")
                .tag("servicio", "estadisticas")
                .register(registry);

        this.estadisticasProgramadasEjecutadas = Counter.builder("estadisticas.programadas.ejecutadas")
                .description("Ejecuciones programadas de generación de estadísticas")
                .tag("servicio", "estadisticas")
                .tag("frecuencia", "1_minuto")
                .register(registry);

        // Gauges específicos
        this.estadisticasEnCache = new AtomicInteger(0);
        registry.gauge("estadisticas.en.cache",
                estadisticasEnCache, AtomicInteger::get);

        this.categoriasMasReportadas = new AtomicInteger(0);
        registry.gauge("estadisticas.categorias.reportadas",
                categoriasMasReportadas, AtomicInteger::get);

        this.solicitudesSpamDetectadas = new AtomicInteger(0);
        registry.gauge("estadisticas.solicitudes.spam",
                solicitudesSpamDetectadas, AtomicInteger::get);

        // Métricas adicionales específicas
        Counter.builder("estadisticas.errores.generacion")
                .description("Errores durante la generación de estadísticas")
                .tag("servicio", "estadisticas")
                .register(registry);

        Counter.builder("estadisticas.errores.exportacion")
                .description("Errores durante la exportación de estadísticas")
                .tag("servicio", "estadisticas")
                .tag("tipo", "csv")
                .register(registry);

        Counter.builder("estadisticas.rangos.fecha.procesados")
                .description("Rangos de fecha procesados en consultas")
                .tag("servicio", "estadisticas")
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

    @Around("execution(* *..*ControllerAdvice.*(..))")
    public Object aroundControllerAdvice(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "exception.handling", "controller_advice");
    }

    // ==================== INTERCEPTORES ESPECÍFICOS DEL MÓDULO ESTADÍSTICAS ====================

    @Around("execution(* *..*EstadisticasService.*(..))")
    public Object aroundEstadisticasService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "estadisticas.generacion.operations", "generacion_estadisticas");
    }

    @Around("execution(* *..*EstadisticasConsultaService.*(..))")
    public Object aroundEstadisticasConsultaService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "estadisticas.consulta.operations", "consulta_estadisticas");
    }

    @Around("execution(* *..*GeneracionCsvService.*(..))")
    public Object aroundGeneracionCsvService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "estadisticas.csv.operations", "generacion_csv");
    }

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object aroundScheduledMethods(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();

        if ("crearEstadisticas".equals(methodName)) {
            estadisticasProgramadasEjecutadas.increment();

            registry.counter("estadisticas.scheduler.executions",
                            "servicio", "estadisticas",
                            "tarea", "generacion_automatica",
                            "frecuencia", "cada_minuto")
                    .increment();
        }

        return recordExecution(pjp, "estadisticas.scheduled.tasks", "scheduled");
    }

    // ==================== MÉTRICAS ESPECÍFICAS DE NEGOCIO ====================

    /**
     * Métricas para generarEstadisticas() - proceso completo de generación
     */
    @AfterReturning("execution(* *..*EstadisticasService.generarEstadisticas(..))")
    public void afterGeneracionCompletada(JoinPoint jp) {
        estadisticasGeneradasTotal.increment();

        registry.counter("estadisticas.generacion.completada",
                        "servicio", "estadisticas",
                        "operacion", "generacion_completa")
                .increment();

        // Incrementar gauge de estadísticas en cache
        estadisticasEnCache.incrementAndGet();
    }

    /**
     * Métricas para errores en generación de estadísticas
     */
    @AfterThrowing(
            pointcut = "execution(* *..*EstadisticasService.generarEstadisticas(..))",
            throwing = "ex"
    )
    public void afterErrorGeneracion(JoinPoint jp, Exception ex) {
        registry.counter("estadisticas.errores.generacion",
                        "servicio", "estadisticas",
                        "exception", ex.getClass().getSimpleName(),
                        "contexto", "generacion_sql")
                .increment();
    }

    /**
     * Métricas para cada consulta SQL individual en generarEstadisticas()
     */
    @AfterReturning("execution(* *..*EstadisticasService.generar*(..))")
    public void afterConsultaSQLIndividual(JoinPoint jp) {
        queriesSQLEjecutadas.increment();

        String methodName = getMethodName(jp);
        String tipoConsulta = extraerTipoConsulta(methodName);

        registry.counter("estadisticas.consulta.sql.individual",
                        "servicio", "estadisticas",
                        "tipo", tipoConsulta,
                        "metodo", methodName)
                .increment();
    }

    /**
     * Métricas para obtenerMayorHechosProvinciaColeccion()
     */
    @AfterReturning(
            pointcut = "execution(* *..*EstadisticasConsultaService.obtenerMayorHechosProvinciaColeccion(..))",
            returning = "result"
    )
    public void afterConsultaMayorHechos(JoinPoint jp, Object result) {
        consultasEstadisticasRealizadas.increment();

        if (result instanceof List<?> list) {
            registry.counter("estadisticas.consulta.resultados",
                            "servicio", "estadisticas",
                            "tipo", "mayor_hechos_provincia_coleccion",
                            "cantidad_registros", String.valueOf(list.size()))
                    .increment();
        }
    }

    /**
     * Métricas para obtenerCategoriaMasReportada()
     */
    @AfterReturning(
            pointcut = "execution(* *..*EstadisticasConsultaService.obtenerCategoriaMasReportada(..))",
            returning = "result"
    )
    public void afterConsultaCategoriaMasReportada(JoinPoint jp, Object result) {
        consultasEstadisticasRealizadas.increment();

        if (result instanceof List<?> list && !list.isEmpty()) {
            categoriasMasReportadas.set(list.size());

            registry.counter("estadisticas.consulta.resultados",
                            "servicio", "estadisticas",
                            "tipo", "categoria_mas_reportada",
                            "cantidad_registros", String.valueOf(list.size()))
                    .increment();
        }
    }

    /**
     * Métricas para obtenerProvinciaPorCategoria()
     */
    @AfterReturning(
            pointcut = "execution(* *..*EstadisticasConsultaService.obtenerProvinciaPorCategoria(..))",
            returning = "result"
    )
    public void afterConsultaProvinciaPorCategoria(JoinPoint jp, Object result) {
        consultasEstadisticasRealizadas.increment();

        if (result instanceof List<?> list) {
            registry.counter("estadisticas.consulta.resultados",
                            "servicio", "estadisticas",
                            "tipo", "provincia_por_categoria",
                            "cantidad_registros", String.valueOf(list.size()))
                    .increment();
        }
    }

    /**
     * Métricas para obtenerHoraPorCategoria()
     */
    @AfterReturning(
            pointcut = "execution(* *..*EstadisticasConsultaService.obtenerHoraPorCategoria(..))",
            returning = "result"
    )
    public void afterConsultaHoraPorCategoria(JoinPoint jp, Object result) {
        consultasEstadisticasRealizadas.increment();

        if (result instanceof List<?> list) {
            registry.counter("estadisticas.consulta.resultados",
                            "servicio", "estadisticas",
                            "tipo", "hora_por_categoria",
                            "cantidad_registros", String.valueOf(list.size()))
                    .increment();
        }
    }

    /**
     * Métricas para obtenerCantidadSolicitudesSpam()
     */
    @AfterReturning(
            pointcut = "execution(* *..*EstadisticasConsultaService.obtenerCantidadSolicitudesSpam(..))",
            returning = "result"
    )
    public void afterConsultaSolicitudesSpam(JoinPoint jp, Object result) {
        consultasEstadisticasRealizadas.increment();

        if (result instanceof List<?> list && !list.isEmpty()) {
            // Actualizar gauge con la cantidad más reciente de spam
            try {
                Object dto = list.get(0);
                if (dto.getClass().getSimpleName().contains("EstadCantidadSolicitudesSpamDTO")) {
                    // Podríamos usar reflexión para obtener el valor, pero por simplicidad incrementamos
                    solicitudesSpamDetectadas.incrementAndGet();
                }
            } catch (Exception e) {
                // Ignorar errores en extracción
            }

            registry.counter("estadisticas.consulta.resultados",
                            "servicio", "estadisticas",
                            "tipo", "solicitudes_spam",
                            "cantidad_registros", String.valueOf(list.size()))
                    .increment();
        }
    }

    /**
     * Métricas para métodos de exportación CSV
     */
    @AfterReturning("execution(* *..*GeneracionCsvService.escribir*Csv(..))")
    public void afterExportacionCSV(JoinPoint jp) {
        csvExportadosTotal.increment();

        String methodName = getMethodName(jp);
        String tipoEstadistica = extraerTipoEstadistica(methodName);

        registry.counter("estadisticas.csv.exportado.individual",
                        "servicio", "estadisticas",
                        "tipo", tipoEstadistica,
                        "formato", "csv")
                .increment();

        // Contar rangos de fecha procesados
        Object[] args = jp.getArgs();
        if (args.length >= 2 && args[1] instanceof LocalDate && args[2] instanceof LocalDate) {
            registry.counter("estadisticas.rangos.fecha.procesados",
                            "servicio", "estadisticas",
                            "tipo", tipoEstadistica)
                    .increment();
        }
    }

    /**
     * Métricas para errores en exportación CSV
     */
    @AfterThrowing(
            pointcut = "execution(* *..*GeneracionCsvService.escribir*Csv(..))",
            throwing = "ex"
    )
    public void afterErrorExportacionCSV(JoinPoint jp, Exception ex) {
        registry.counter("estadisticas.errores.exportacion",
                        "servicio", "estadisticas",
                        "exception", ex.getClass().getSimpleName(),
                        "tipo", extraerTipoEstadistica(getMethodName(jp)))
                .increment();
    }

    /**
     * Métricas para métodos que procesan OutputStream
     */
    @AfterReturning(
            pointcut = "execution(* *..*GeneracionCsvService.escribir*Csv(OutputStream, ..))",
            returning = "result"
    )
    public void afterProcesamientoOutputStream(JoinPoint jp, Object result) {
        String methodName = getMethodName(jp);

        registry.counter("estadisticas.outputstream.procesado",
                        "servicio", "estadisticas",
                        "tipo", extraerTipoEstadistica(methodName),
                        "operacion", "escritura_csv")
                .increment();
    }

    /**
     * Métricas para queries complejas (con subqueries)
     */
    @After("execution(* *..*EstadisticasService.generar*(Statement, ..))")
    public void afterQueryCompleta(JoinPoint jp) {
        String methodName = getMethodName(jp);

        if (methodName.contains("MayorCantidadHechos") ||
                methodName.contains("ProvinciaPorCategoria") ||
                methodName.contains("HoraPorCategoria")) {

            registry.counter("estadisticas.queries.complejas",
                            "servicio", "estadisticas",
                            "tipo", "con_subqueries",
                            "metodo", methodName)
                    .increment();
        }
    }

    /**
     * Métricas para uso de BOM en CSV
     */
    @AfterReturning("execution(* *..*GeneracionCsvService.escribir*Csv(OutputStream, ..))")
    public void afterBOMEscrito(JoinPoint jp) {
        registry.counter("estadisticas.csv.bom.escrito",
                        "servicio", "estadisticas",
                        "tipo", extraerTipoEstadistica(getMethodName(jp)),
                        "encoding", "utf8_bom")
                .increment();
    }

    /**
     * Métricas para scheduler de estadísticas
     */
    @AfterReturning("execution(* *..*GeneracionEstadisticasScheduled.crearEstadisticas(..))")
    public void afterSchedulerEjecutado(JoinPoint jp) {
        registry.counter("estadisticas.scheduler.completado",
                        "servicio", "estadisticas",
                        "tarea", "generacion_estadisticas",
                        "resultado", "exitoso")
                .increment();
    }

    /**
     * Métricas para errores en scheduler
     */
    @AfterThrowing(
            pointcut = "execution(* *..*GeneracionEstadisticasScheduled.crearEstadisticas(..))",
            throwing = "ex"
    )
    public void afterErrorScheduler(JoinPoint jp, Exception ex) {
        registry.counter("estadisticas.scheduler.error",
                        "servicio", "estadisticas",
                        "exception", ex.getClass().getSimpleName(),
                        "tarea", "generacion_estadisticas")
                .increment();
    }

    /**
     * Métricas para consultas por rango de fecha
     */
    @After("execution(* *..*findByFechaCalculoBetween(..))")
    public void afterConsultaPorRango(JoinPoint jp) {
        Object[] args = jp.getArgs();
        if (args.length >= 2 &&
                args[0] instanceof LocalDateTime &&
                args[1] instanceof LocalDateTime) {

            registry.counter("estadisticas.consultas.por.rango",
                            "servicio", "estadisticas",
                            "tipo", "fecha_calculo_between")
                    .increment();
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
                    "servicio", "estadisticas",
                    "class", className,
                    "method", method,
                    "result", "success"));

            // Registrar contador de éxitos
            incrementCounter(metricName + ".count",
                    "kind", kind,
                    "servicio", "estadisticas",
                    "class", className,
                    "method", method,
                    "result", "success");

            return result;

        } catch (Throwable t) {
            // Registrar timer de error
            sample.stop(getOrCreateTimer(metricName + ".seconds",
                    "kind", kind,
                    "servicio", "estadisticas",
                    "class", className,
                    "method", method,
                    "result", "error"));

            // Registrar contador de errores
            String exceptionName = t.getClass().getSimpleName();
            incrementCounter(metricName + ".errors",
                    "exception", exceptionName,
                    "kind", kind,
                    "servicio", "estadisticas",
                    "class", className,
                    "method", method);

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

    private String extraerTipoConsulta(String methodName) {
        if (methodName.contains("MayorCantidadHechos")) return "mayor_hechos_provincia_coleccion";
        if (methodName.contains("CategoriaMasReportada")) return "categoria_mas_reportada";
        if (methodName.contains("ProvinciaPorCategoria")) return "provincia_por_categoria";
        if (methodName.contains("HoraPorCategoria")) return "hora_por_categoria";
        if (methodName.contains("CantidadSolicitudesSpam")) return "solicitudes_spam";
        return "desconocida";
    }

    private String extraerTipoEstadistica(String methodName) {
        if (methodName.contains("MayorHechosProvinciaColeccion")) return "mayor_hechos_provincia_coleccion";
        if (methodName.contains("CategoriaMasReportada")) return "categoria_mas_reportada";
        if (methodName.contains("ProvinciaPorCategoria")) return "provincia_por_categoria";
        if (methodName.contains("HoraPorCategoria")) return "hora_por_categoria";
        if (methodName.contains("CantidadSolicitudesSpam")) return "solicitudes_spam";
        return "desconocida";
    }
}