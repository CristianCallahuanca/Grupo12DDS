package org.example.metamapa.metrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.metamapa.models.dtos.HechoDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class MetamapaMetricsAspect {

    private final MeterRegistry registry;

    // Cache para performance
    private final Map<String, Timer> timerCache = new ConcurrentHashMap<>();
    private final Map<String, Counter> counterCache = new ConcurrentHashMap<>();

    // ==================== MÉTRICAS ESPECÍFICAS DE METAMAPA ====================
    private final Counter fuentesMetamapaConsultadas;
    private final Counter hechosMetamapaObtenidos;
    private final Counter fuentesMetamapaRegistradas;
    private final Counter conexionesMetamapaExitosas;
    private final Counter conexionesMetamapaFallidas;

    public MetamapaMetricsAspect(MeterRegistry registry) {
        this.registry = registry;

        // Inicializar métricas ESPECÍFICAS de metamapa
        this.fuentesMetamapaConsultadas = Counter.builder("metamapa.fuentes.consultadas")
                .description("Total de fuentes MetaMapa consultadas")
                .tag("servicio", "loader-metamapa")
                .register(registry);

        this.hechosMetamapaObtenidos = Counter.builder("metamapa.hechos.obtenidos")
                .description("Total de hechos obtenidos de fuentes MetaMapa")
                .tag("servicio", "loader-metamapa")
                .register(registry);

        this.fuentesMetamapaRegistradas = Counter.builder("metamapa.fuentes.registradas")
                .description("Fuentes MetaMapa registradas en el sistema")
                .tag("servicio", "loader-metamapa")
                .register(registry);

        this.conexionesMetamapaExitosas = Counter.builder("metamapa.conexiones.exitosas")
                .description("Conexiones exitosas a instancias MetaMapa remotas")
                .tag("servicio", "loader-metamapa")
                .register(registry);

        this.conexionesMetamapaFallidas = Counter.builder("metamapa.conexiones.fallidas")
                .description("Conexiones fallidas a instancias MetaMapa remotas")
                .tag("servicio", "loader-metamapa")
                .register(registry);

        // Métricas adicionales específicas
        Counter.builder("metamapa.registro.intentos")
                .description("Intentos de registro en el agregador")
                .tag("servicio", "loader-metamapa")
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

    // ==================== INTERCEPTORES ESPECÍFICOS DE METAMAPA ====================

    @Around("execution(* *..*AdapterMetamapa.*(..))")
    public Object aroundAdapterMethods(ProceedingJoinPoint pjp) throws Throwable {
        fuentesMetamapaConsultadas.increment();
        return recordExecution(pjp, "metamapa.adapter.calls", "adapter");
    }

    @Around("execution(* *..*CargaMetamapaService.*(..))")
    public Object aroundCargaMetamapa(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "metamapa.carga.operations", "carga");
    }

    @Around("execution(* *..*RegistroFuenteService.*(..))")
    public Object aroundRegistroFuente(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "metamapa.registro.operations", "registro");
    }

    // ==================== MÉTRICAS DE NEGOCIO ESPECÍFICAS ====================

    /**
     * Contar hechos obtenidos de MetaMapa
     */
    @AfterReturning(
            pointcut = "execution(* *..*CargaMetamapaService.obtenerHechos(..))",
            returning = "result"
    )
    public void afterObtenerHechos(JoinPoint jp, Object result) {
        if (result instanceof List<?> list) {
            if (!list.isEmpty() && list.get(0) instanceof HechoDTO) {
                hechosMetamapaObtenidos.increment(list.size());

                // Métrica adicional: distribución por cantidad
                registry.counter("metamapa.hechos.por.consulta",
                                "servicio", "loader-metamapa",
                                "cantidad", String.valueOf(list.size()))
                        .increment();
            }
        }
    }

    /**
     * Contar fuentes registradas
     */
    @AfterReturning(
            pointcut = "execution(* *..*FuentesMetamapaService.registrarFuenteMetamapa(..))",
            returning = "result"
    )
    public void afterRegistrarFuente(JoinPoint jp, Object result) {
        if (result != null) {
            fuentesMetamapaRegistradas.increment();
        }
    }

    /**
     * Contar conexiones exitosas al adaptador
     */
    @AfterReturning("execution(* *..*IAdapterMetamapa.obtenerHechos(..))")
    public void afterConexionExitosa(JoinPoint jp) {
        conexionesMetamapaExitosas.increment();
    }

    /**
     * Contar errores de conexión a MetaMapa
     */
    @AfterThrowing(
            pointcut = "execution(* *..*CargaMetamapaService.*(..)) || " +
                    "execution(* *..*IAdapterMetamapa.*(..))",
            throwing = "ex"
    )
    public void handleMetamapaErrors(JoinPoint jp, Exception ex) {
        String exceptionName = ex.getClass().getSimpleName();

        if (exceptionName.contains("Conexion") || exceptionName.contains("Connection")) {
            conexionesMetamapaFallidas.increment();

            registry.counter("metamapa.errores.conexion",
                            "exception", exceptionName,
                            "servicio", "loader-metamapa",
                            "metodo", getMethodName(jp))
                    .increment();
        }
    }

    /**
     * Métricas para intentos de registro en agregador
     */
    @AfterReturning("execution(* *..*RegistroFuenteService.intentarRegistro(..))")
    public void afterIntentoRegistroExitoso(JoinPoint jp) {
        registry.counter("metamapa.registro.intentos",
                        "servicio", "loader-metamapa",
                        "estado", "exitoso")
                .increment();
    }

    @AfterThrowing(
            pointcut = "execution(* *..*RegistroFuenteService.intentarRegistro(..))",
            throwing = "ex"
    )
    public void afterIntentoRegistroFallido(JoinPoint jp, Exception ex) {
        registry.counter("metamapa.registro.intentos",
                        "servicio", "loader-metamapa",
                        "estado", "fallido",
                        "exception", ex.getClass().getSimpleName())
                .increment();
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
                    "servicio", "loader-metamapa",
                    "class", className,
                    "method", method,
                    "result", "success"));

            // Registrar contador de éxitos
            incrementCounter(metricName + ".count",
                    "kind", kind,
                    "servicio", "loader-metamapa",
                    "class", className,
                    "method", method,
                    "result", "success");

            return result;

        } catch (Throwable t) {
            // Registrar timer de error
            sample.stop(getOrCreateTimer(metricName + ".seconds",
                    "kind", kind,
                    "servicio", "loader-metamapa",
                    "class", className,
                    "method", method,
                    "result", "error"));

            // Registrar contador de errores
            String exceptionName = t.getClass().getSimpleName();
            incrementCounter(metricName + ".errors",
                    "exception", exceptionName,
                    "kind", kind,
                    "servicio", "loader-metamapa",
                    "class", className,
                    "method", method);

            // Métricas específicas para errores críticos
            if (exceptionName.contains("Timeout") || exceptionName.contains("Connection")) {
                incrementCounter("metamapa.errors.critical",
                        "type", "connection",
                        "servicio", "loader-metamapa");
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