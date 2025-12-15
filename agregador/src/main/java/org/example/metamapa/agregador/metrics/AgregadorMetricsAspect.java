package org.example.metamapa.agregador.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.aspectj.lang.reflect.MethodSignature;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class AgregadorMetricsAspect {

    private final MeterRegistry registry;

    // Cache para performance
    private final Map<String, Timer> timerCache = new ConcurrentHashMap<>();
    private final Map<String, Counter> counterCache = new ConcurrentHashMap<>();

    // ==================== MÉTRICAS ESPECÍFICAS DEL AGREGADOR ====================
    private final Counter hechosIntegradosTotal;
    private final Counter hechosNormalizadosTotal;
    private final Counter hechosDuplicadosEliminados;
    private final Counter fuentesConsultadas;
    private final Counter hechosProcesadosPorIntegracion;
    private final Counter integracionesCompletadas;
    private final Counter integracionesFallidas;

    // Gauges específicos
    private final AtomicInteger hechosEnRevision;
    private final AtomicInteger hechosVisibles;
    private final AtomicInteger categoriasCacheadas;
    private final AtomicInteger provinciasCargadas;

    public AgregadorMetricsAspect(MeterRegistry registry) {
        this.registry = registry;

        // Inicializar métricas ESPECÍFICAS del agregador
        this.hechosIntegradosTotal = Counter.builder("agregador.hechos.integrados")
                .description("Total de hechos integrados de todas las fuentes")
                .tag("servicio", "agregador")
                .register(registry);

        this.hechosNormalizadosTotal = Counter.builder("agregador.hechos.normalizados")
                .description("Hechos normalizados exitosamente")
                .tag("servicio", "agregador")
                .register(registry);

        this.hechosDuplicadosEliminados = Counter.builder("agregador.hechos.duplicados.eliminados")
                .description("Hechos eliminados por ser duplicados")
                .tag("servicio", "agregador")
                .register(registry);

        this.fuentesConsultadas = Counter.builder("agregador.fuentes.consultadas")
                .description("Fuentes consultadas para integración")
                .tag("servicio", "agregador")
                .register(registry);

        this.hechosProcesadosPorIntegracion = Counter.builder("agregador.integraciones.hechos.procesados")
                .description("Hechos procesados en cada integración")
                .tag("servicio", "agregador")
                .register(registry);

        this.integracionesCompletadas = Counter.builder("agregador.integraciones.completadas")
                .description("Integraciones completadas exitosamente")
                .tag("servicio", "agregador")
                .register(registry);

        this.integracionesFallidas = Counter.builder("agregador.integraciones.fallidas")
                .description("Integraciones que fallaron")
                .tag("servicio", "agregador")
                .register(registry);

        // Gauges específicos
        this.hechosEnRevision = new AtomicInteger(0);
        registry.gauge("agregador.hechos.en_revision",
                hechosEnRevision, AtomicInteger::get);

        this.hechosVisibles = new AtomicInteger(0);
        registry.gauge("agregador.hechos.visibles",
                hechosVisibles, AtomicInteger::get);

        this.categoriasCacheadas = new AtomicInteger(0);
        registry.gauge("agregador.categorias.cacheadas",
                categoriasCacheadas, AtomicInteger::get);

        this.provinciasCargadas = new AtomicInteger(0);
        registry.gauge("agregador.provincias.cargadas",
                provinciasCargadas, AtomicInteger::get);

        // Métricas adicionales específicas
        Counter.builder("agregador.errores.normalizacion")
                .description("Errores durante la normalización de hechos")
                .tag("servicio", "agregador")
                .register(registry);

        Counter.builder("agregador.errores.conexion.fuente")
                .description("Errores de conexión a fuentes")
                .tag("servicio", "agregador")
                .tag("tipo", "timeout")
                .register(registry);

        Counter.builder("agregador.hechos.sincategoria")
                .description("Hechos sin categoría asignada")
                .tag("servicio", "agregador")
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

    // ==================== INTERCEPTORES ESPECÍFICOS DEL AGREGADOR ====================

    @Around("execution(* *..*AgregacionService.*(..))")
    public Object aroundAgregacionService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "agregador.agregacion.operations", "agregacion");
    }

    @Around("execution(* *..*NormalizacionService.*(..))")
    public Object aroundNormalizacionService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "agregador.normalizacion.operations", "normalizacion");
    }

    @Around("execution(* *..*DuplicacionService.*(..))")
    public Object aroundDuplicacionService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "agregador.duplicacion.operations", "duplicacion");
    }

    @Around("execution(* *..*CatalogoCategoriasService.*(..))")
    public Object aroundCatalogoCategoriasService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "agregador.categorias.operations", "catalogo_categorias");
    }

    @Around("execution(* *..*FuentesService.*(..))")
    public Object aroundFuentesService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "agregador.fuentes.operations", "fuentes");
    }

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object aroundScheduledMethods(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();
        String metricName = "agregador.scheduled.tasks";

        if ("obtenerHechosTodasLasFuentes".equals(methodName)) {
            metricName = "agregador.integracion.scheduled";
        } else if ("limpiarYReetiquetarHechos".equals(methodName)) {
            metricName = "agregador.limpieza.scheduled";
        } else if ("generarSugerencias".equals(methodName)) {
            metricName = "agregador.sugerencias.scheduled";
        }

        return recordExecution(pjp, metricName, "scheduled");
    }

    // ==================== MÉTRICAS ESPECÍFICAS DE NEGOCIO ====================

    /**
     * Métricas para integrarHechosFuentes() - proceso completo de integración
     */
    @AfterReturning("execution(* *..*AgregacionService.integrarHechosFuentes(..))")
    public void afterIntegracionCompletada(JoinPoint jp) {
        integracionesCompletadas.increment();

        registry.counter("agregador.proceso.completado",
                        "servicio", "agregador",
                        "operacion", "integracion_completa")
                .increment();
    }

    /**
     * Métricas para errores en integración
     */
    @AfterThrowing(
            pointcut = "execution(* *..*AgregacionService.integrarHechosFuentes(..))",
            throwing = "ex"
    )
    public void afterIntegracionFallida(JoinPoint jp, Exception ex) {
        integracionesFallidas.increment();

        registry.counter("agregador.proceso.error",
                        "servicio", "agregador",
                        "exception", ex.getClass().getSimpleName(),
                        "contexto", "integracion")
                .increment();
    }

    /**
     * Métricas para obtenerHechosDeFuentesParalelo()
     */
    @AfterReturning(
            pointcut = "execution(* *..*AgregacionService.obtenerHechosDeFuentesParalelo(..))",
            returning = "result"
    )
    public void afterObtenerHechosFuentes(JoinPoint jp, Object result) {
        if (result instanceof List<?> list) {
            int cantidad = list.size();
            hechosIntegradosTotal.increment(cantidad);
            hechosProcesadosPorIntegracion.increment(cantidad);

            registry.counter("agregador.fuentes.consulta.batch",
                            "servicio", "agregador",
                            "cantidad_hechos", String.valueOf(cantidad),
                            "fuente", "todas_paralelo")
                    .increment();

            // Histograma del tamaño del batch
            registry.summary("agregador.batch.size.hechos",
                            "servicio", "agregador")
                    .record(cantidad);
        }
    }

    /**
     * Métricas para cada fuente consultada individualmente
     */
    @AfterReturning(
            pointcut = "execution(* *..*AgregacionService.obtenerHechosFuenteAsync(..))",
            returning = "result"
    )
    public void afterConsultaFuenteIndividual(JoinPoint jp, Object result) {
        fuentesConsultadas.increment();

        if (result != null) {
            Object[] args = jp.getArgs();
            String fuenteNombre = args.length > 0 ? args[0].toString() : "desconocida";

            registry.counter("agregador.fuente.consulta.individual",
                            "servicio", "agregador",
                            "fuente", fuenteNombre,
                            "resultado", "exitoso")
                    .increment();
        }
    }

    /**
     * Métricas para normalizarHecho()
     */
    @AfterReturning(
            pointcut = "execution(* *..*NormalizacionService.normalizarHecho(..))",
            returning = "result"
    )
    public void afterNormalizacionExitosa(JoinPoint jp, Object result) {
        if (result != null) {
            hechosNormalizadosTotal.increment();

            Object[] args = jp.getArgs();
            String titulo = args.length > 0 ? args[0].toString() : "sin_titulo";

            registry.counter("agregador.hecho.normalizado.individual",
                            "servicio", "agregador",
                            "titulo", titulo.length() > 50 ? titulo.substring(0, 50) : titulo,
                            "resultado", "exitoso")
                    .increment();
        }
    }

    /**
     * Métricas para errores en normalización
     */
    @AfterThrowing(
            pointcut = "execution(* *..*NormalizacionService.normalizarHecho(..))",
            throwing = "ex"
    )
    public void afterErrorNormalizacion(JoinPoint jp, Exception ex) {
        registry.counter("agregador.errores.normalizacion",
                        "servicio", "agregador",
                        "exception", ex.getClass().getSimpleName(),
                        "campo", extraerCampoError(ex))
                .increment();
    }

    /**
     * Métricas para eliminarHechosRepetidos()
     */
    @AfterReturning(
            pointcut = "execution(* *..*DuplicacionService.eliminarHechosRepetidos(..))",
            returning = "result"
    )
    public void afterDuplicacionProcesada(JoinPoint jp, Object result) {
        if (result instanceof List<?> list) {
            Object[] args = jp.getArgs();
            if (args.length > 0 && args[0] instanceof List) {
                @SuppressWarnings("unchecked")
                int originalSize = ((List<?>) args[0]).size();
                int eliminados = originalSize - list.size();

                if (eliminados > 0) {
                    hechosDuplicadosEliminados.increment(eliminados);

                    registry.counter("agregador.duplicados.detectados",
                                    "servicio", "agregador",
                                    "cantidad_eliminados", String.valueOf(eliminados),
                                    "porcentaje", String.format("%.1f", (eliminados * 100.0 / originalSize)))
                            .increment();
                }
            }
        }
    }

    /**
     * Métricas para validarVisibilidadPorCategoria()
     */
    @AfterReturning("execution(* *..*AgregacionService.validarVisibilidadPorCategoria(..))")
    public void afterValidacionVisibilidad(JoinPoint jp) {
        Object[] args = jp.getArgs();
        if (args.length > 0) {
            // Actualizar gauges basados en el estado del hecho
            registry.counter("agregador.visibilidad.validada",
                            "servicio", "agregador")
                    .increment();
        }
    }

    /**
     * Métricas para guardarHechosSeguro()
     */
    @AfterReturning(
            pointcut = "execution(* *..*AgregacionService.guardarHechosSeguro(..))",
            returning = "result"
    )
    public void afterGuardadoHechos(JoinPoint jp, Object result) {
        Object[] args = jp.getArgs();
        if (args.length > 0 && args[0] instanceof List) {
            @SuppressWarnings("unchecked")
            int guardados = ((List<?>) args[0]).size();

            registry.counter("agregador.hechos.persistidos",
                            "servicio", "agregador",
                            "cantidad", String.valueOf(guardados),
                            "operacion", "batch_persist")
                    .increment();

            // Actualizar gauges de estado
            // (en un sistema real, esto vendría de una consulta a la DB)
        }
    }

    /**
     * Métricas para inicializarCatalogo()
     */
    @AfterReturning(
            pointcut = "execution(* *..*CatalogoCategoriasService.inicializarCatalogo(..))",
            returning = "result"
    )
    public void afterInicializarCatalogo(JoinPoint jp, Object result) {
        Object[] args = jp.getArgs();
        // El método carga categorías en memoria
        registry.counter("agregador.catalogo.inicializado",
                        "servicio", "agregador",
                        "operacion", "carga_memoria")
                .increment();
    }

    /**
     * Métricas para registrarFuente()
     */
    @AfterReturning("execution(* *..*FuentesService.registrarFuente(..))")
    public void afterRegistrarFuente(JoinPoint jp) {
        registry.counter("agregador.fuente.registrada",
                        "servicio", "agregador",
                        "tipo", "nueva_fuente")
                .increment();
    }

    /**
     * Métricas para ProvinciaLocator.init()
     */
    @AfterReturning("execution(* *..*ProvinciaLocator.init(..))")
    public void afterInicializarProvincias(JoinPoint jp) {
        registry.counter("agregador.provincias.inicializadas",
                        "servicio", "agregador")
                .increment();
    }

    /**
     * Métricas para obtenerProvincia()
     */
    @AfterReturning(
            pointcut = "execution(* *..*ProvinciaLocator.obtenerProvincia(..))",
            returning = "result"
    )
    public void afterGeolocalizacion(JoinPoint jp, Object result) {
        String provincia = result != null ? result.toString() : "no_encontrada";

        registry.counter("agregador.geolocalizacion.ejecutada",
                        "servicio", "agregador",
                        "provincia", provincia,
                        "resultado", provincia.equals("no_encontrada") ? "fallida" : "exitosa")
                .increment();
    }

    /**
     * Métricas para limpiarYReetiquetarHechos()
     */
    @AfterReturning("execution(* *..*limpiarYRecategorizar.limpiarYReetiquetarHechos(..))")
    public void afterLimpiezaNocturna(JoinPoint jp) {
        registry.counter("agregador.limpieza.completada",
                        "servicio", "agregador",
                        "tipo", "nocturna",
                        "operacion", "deduplicacion_recategorizacion")
                .increment();
    }

    /**
     * Métricas para generarSugerencias()
     */
    @AfterReturning("execution(* *..*SugerenciasCategoriasScheduler.generarSugerencias(..))")
    public void afterGenerarSugerencias(JoinPoint jp) {
        registry.counter("agregador.sugerencias.generadas",
                        "servicio", "agregador",
                        "tipo", "categorias",
                        "periodicidad", "cada_3_dias")
                .increment();
    }

    /**
     * Métricas para tiempo de procesamiento paralelo
     */
    @Around("execution(* *..*AgregacionService.*Paralelo*(..))")
    public Object aroundProcesamientoParalelo(ProceedingJoinPoint pjp) throws Throwable {
        Timer.Sample sample = Timer.start(registry);

        try {
            Object result = pjp.proceed();

            sample.stop(registry.timer("agregador.procesamiento.paralelo.duration",
                    "servicio", "agregador",
                    "metodo", pjp.getSignature().getName(),
                    "result", "success"));

            return result;

        } catch (Exception e) {
            sample.stop(registry.timer("agregador.procesamiento.paralelo.duration",
                    "servicio", "agregador",
                    "metodo", pjp.getSignature().getName(),
                    "result", "error",
                    "exception", e.getClass().getSimpleName()));

            throw e;
        }
    }

    /**
     * Métricas para errores de timeout en fuentes
     */
    @AfterThrowing(
            pointcut = "execution(* *..*obtenerHechosFuenteAsync(..))",
            throwing = "ex"
    )
    public void afterTimeoutFuente(JoinPoint jp, Exception ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("timeout")) {
            registry.counter("agregador.errores.conexion.fuente",
                            "servicio", "agregador",
                            "tipo", "timeout",
                            "fuente", getArgOrDefault(jp, 0, "desconocida"))
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
                    "servicio", "agregador",
                    "class", className,
                    "method", method,
                    "result", "success"));

            // Registrar contador de éxitos
            incrementCounter(metricName + ".count",
                    "kind", kind,
                    "servicio", "agregador",
                    "class", className,
                    "method", method,
                    "result", "success");

            return result;

        } catch (Throwable t) {
            // Registrar timer de error
            sample.stop(getOrCreateTimer(metricName + ".seconds",
                    "kind", kind,
                    "servicio", "agregador",
                    "class", className,
                    "method", method,
                    "result", "error"));

            // Registrar contador de errores
            String exceptionName = t.getClass().getSimpleName();
            incrementCounter(metricName + ".errors",
                    "exception", exceptionName,
                    "kind", kind,
                    "servicio", "agregador",
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

    private String getArgOrDefault(JoinPoint jp, int index, String defaultValue) {
        if (jp.getArgs() != null && jp.getArgs().length > index) {
            Object arg = jp.getArgs()[index];
            return arg != null ? arg.toString() : defaultValue;
        }
        return defaultValue;
    }

    private String extraerCampoError(Exception ex) {
        String message = ex.getMessage();
        if (message == null) return "desconocido";

        if (message.contains("latitud") || message.contains("longitud")) return "ubicacion";
        if (message.contains("fecha")) return "fecha";
        if (message.contains("categoría") || message.contains("categoria")) return "categoria";
        if (message.contains("titulo")) return "titulo";
        if (message.contains("descripcion")) return "descripcion";

        return "general";
    }
}