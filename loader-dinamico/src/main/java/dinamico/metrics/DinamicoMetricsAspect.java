package dinamico.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class DinamicoMetricsAspect {

    private final MeterRegistry registry;

    // Cache para performance
    private final Map<String, Timer> timerCache = new ConcurrentHashMap<>();
    private final Map<String, Counter> counterCache = new ConcurrentHashMap<>();

    // ==================== MÉTRICAS ESPECÍFICAS DEL LOADER DINÁMICO ====================
    private final Counter hechosDinamicosObtenidos;
    private final Counter hechosDinamicosCargados;
    private final Counter registrosDinamicosExitosos;
    private final Counter registrosDinamicosFallidos;
    private final Counter archivosDinamicosSubidos;
    private final Counter dbDinamicaVaciada;

    // Gauges específicos
    private final AtomicInteger hechosDinamicosPendientes;
    private final AtomicInteger hechosDinamicosPorBatch;

    public DinamicoMetricsAspect(MeterRegistry registry) {
        this.registry = registry;

        // Inicializar métricas ESPECÍFICAS del loader dinámico
        this.hechosDinamicosObtenidos = Counter.builder("loader.dinamico.hechos.obtenidos")
                .description("Total de hechos obtenidos para enviar al agregador")
                .tag("servicio", "loader-dinamico")
                .tag("tipo_fuente", "DINAMICA")
                .register(registry);

        this.hechosDinamicosCargados = Counter.builder("loader.dinamico.hechos.cargados")
                .description("Total de hechos cargados por contribuyentes")
                .tag("servicio", "loader-dinamico")
                .tag("origen", "Contribuyente Metamapa")
                .register(registry);

        this.registrosDinamicosExitosos = Counter.builder("loader.dinamico.registros.exitosos")
                .description("Registros exitosos en el agregador")
                .tag("servicio", "loader-dinamico")
                .tag("tipo_registro", "self-registration")
                .register(registry);

        this.registrosDinamicosFallidos = Counter.builder("loader.dinamico.registros.fallidos")
                .description("Registros fallidos en el agregador")
                .tag("servicio", "loader-dinamico")
                .tag("tipo_registro", "self-registration")
                .register(registry);

        this.archivosDinamicosSubidos = Counter.builder("loader.dinamico.archivos.subidos")
                .description("Archivos multimedia subidos a Cloudinary")
                .tag("servicio", "loader-dinamico")
                .tag("provider", "cloudinary")
                .register(registry);

        this.dbDinamicaVaciada = Counter.builder("loader.dinamico.db.vaciada")
                .description("Veces que se vació la base de datos de hechos crudos")
                .tag("servicio", "loader-dinamico")
                .tag("operacion", "cleanup")
                .register(registry);

        // Gauges específicos
        this.hechosDinamicosPendientes = new AtomicInteger(0);
        registry.gauge("loader.dinamico.hechos.pendientes",
                hechosDinamicosPendientes, AtomicInteger::get);

        this.hechosDinamicosPorBatch = new AtomicInteger(0);
        registry.gauge("loader.dinamico.hechos.por.batch",
                hechosDinamicosPorBatch, AtomicInteger::get);

        // Métricas adicionales específicas
        Counter.builder("loader.dinamico.registro.retry")
                .description("Reintentos de registro en el agregador")
                .tag("servicio", "loader-dinamico")
                .tag("estrategia", "scheduled-retry")
                .register(registry);

        Counter.builder("loader.dinamico.file.upload.errors")
                .description("Errores en subida de archivos")
                .tag("servicio", "loader-dinamico")
                .tag("provider", "cloudinary")
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

    // ==================== INTERCEPTORES ESPECÍFICOS DEL LOADER DINÁMICO ====================

    @Around("execution(* *..*RegistroFuenteService.*(..))")
    public Object aroundRegistroFuenteService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "loader.dinamico.registro.operations", "registro_fuente");
    }

    @Around("execution(* *..*HechosService.*(..))")
    public Object aroundHechosService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "loader.dinamico.hechos.operations", "hechos_service");
    }

    @Around("execution(* *..*FileUploadService.*(..))")
    public Object aroundFileUploadService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "loader.dinamico.file.upload.operations", "file_upload");
    }

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object aroundScheduledMethods(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();
        if ("retryRegistro".equals(methodName)) {
            registry.counter("loader.dinamico.registro.retry",
                            "servicio", "loader-dinamico",
                            "metodo", methodName)
                    .increment();
        }
        return recordExecution(pjp, "loader.dinamico.scheduled.tasks", "scheduled");
    }

    // ==================== MÉTRICAS ESPECÍFICAS DE NEGOCIO ====================

    /**
     * Métricas para el método obtenerHechos() - envía hechos al agregador
     */
    @AfterReturning(
            pointcut = "execution(* *..*HechosService.obtenerHechos(..))",
            returning = "result"
    )
    public void afterObtenerHechos(JoinPoint jp, Object result) {
        if (result instanceof List<?> list) {
            int cantidad = list.size();
            hechosDinamicosObtenidos.increment(cantidad);
            hechosDinamicosPorBatch.set(cantidad);

            // Actualizar gauge de hechos pendientes (se acaban de marcar como leídos)
            hechosDinamicosPendientes.set(0);

            // Métrica adicional: distribución por tamaño de lote
            registry.counter("loader.dinamico.hechos.batch.size",
                            "servicio", "loader-dinamico",
                            "tamaño", cantidad > 0 ? "con_datos" : "vacio",
                            "cantidad_exacta", String.valueOf(cantidad))
                    .increment();

            // Histograma del tamaño del batch
            registry.summary("loader.dinamico.batch.size.bytes",
                            "servicio", "loader-dinamico")
                    .record(cantidad);
        }
    }

    /**
     * Métricas para cargarHecho() - recibe hechos de contribuyentes
     */
    @AfterReturning("execution(* *..*HechosService.cargarHecho(..))")
    public void afterCargarHecho(JoinPoint jp) {
        hechosDinamicosCargados.increment();
        hechosDinamicosPendientes.incrementAndGet();

        // Contar archivos multimedia si están en los argumentos
        Object[] args = jp.getArgs();
        if (args.length >= 2 && args[1] instanceof List) {
            @SuppressWarnings("unchecked")
            List<MultipartFile> files = (List<MultipartFile>) args[1];
            if (files != null) {
                registry.counter("loader.dinamico.hecho.con.archivos",
                                "servicio", "loader-dinamico",
                                "cantidad_archivos", String.valueOf(files.size()))
                        .increment();
            }
        }
    }

    /**
     * Métricas para vaciarDB()
     */
    @AfterReturning("execution(* *..*HechosService.vaciarDB(..))")
    public void afterVaciarDB(JoinPoint jp) {
        dbDinamicaVaciada.increment();

        // Resetear gauge de hechos pendientes
        hechosDinamicosPendientes.set(0);

        registry.counter("loader.dinamico.operaciones.admin",
                        "servicio", "loader-dinamico",
                        "tipo", "limpieza_db",
                        "impacto", "todos_los_hechos")
                .increment();
    }

    /**
     * Métricas para registro exitoso en el agregador
     */
    @AfterReturning("execution(* *..*RegistroFuenteService.intentarRegistro(..))")
    public void afterRegistroExitoso(JoinPoint jp) {
        registrosDinamicosExitosos.increment();

        Object[] args = jp.getArgs();
        String origen = args.length > 0 ? String.valueOf(args[0]) : "desconocido";

        registry.counter("loader.dinamico.registro.completado",
                        "servicio", "loader-dinamico",
                        "origen", origen,
                        "estado", "registrado")
                .increment();
    }

    /**
     * Métricas para registro fallido
     */
    @AfterThrowing(
            pointcut = "execution(* *..*RegistroFuenteService.intentarRegistro(..))",
            throwing = "ex"
    )
    public void afterRegistroFallido(JoinPoint jp, Exception ex) {
        registrosDinamicosFallidos.increment();

        registry.counter("loader.dinamico.registro.error",
                        "servicio", "loader-dinamico",
                        "exception", ex.getClass().getSimpleName(),
                        "causa", ex.getMessage() != null ? ex.getMessage() : "unknown")
                .increment();
    }

    /**
     * Métricas para subida exitosa de archivos
     */
    @AfterReturning(
            pointcut = "execution(* *..*FileUploadService.upload(..))",
            returning = "url"
    )
    public void afterFileUploadSuccess(JoinPoint jp, String url) {
        archivosDinamicosSubidos.increment();

        // Medir éxito de uploads
        if (url != null && !url.isEmpty()) {
            registry.counter("loader.dinamico.file.upload.success",
                            "servicio", "loader-dinamico",
                            "resultado", "url_generada")
                    .increment();
        }
    }

    /**
     * Métricas para errores en subida de archivos
     */
    @AfterThrowing(
            pointcut = "execution(* *..*FileUploadService.upload(..))",
            throwing = "ex"
    )
    public void afterFileUploadError(JoinPoint jp, Exception ex) {
        registry.counter("loader.dinamico.file.upload.errors",
                        "servicio", "loader-dinamico",
                        "exception", ex.getClass().getSimpleName())
                .increment();
    }

    /**
     * Métricas para conversiones de DTO
     */
    @AfterReturning(
            pointcut = "execution(* *..*HechosService.crudoDTOOuts(..)) || " +
                    "execution(* *..*HechosService.crudoADTOOut(..))",
            returning = "result"
    )
    public void afterDtoConversion(JoinPoint jp, Object result) {
        String methodName = getMethodName(jp);

        if ("crudoDTOOuts".equals(methodName) && result instanceof List<?> list) {
            registry.counter("loader.dinamico.dto.conversions.batch",
                            "servicio", "loader-dinamico",
                            "cantidad", String.valueOf(list.size()))
                    .increment();
        } else if ("crudoADTOOut".equals(methodName) && result != null) {
            registry.counter("loader.dinamico.dto.conversions.single",
                            "servicio", "loader-dinamico")
                    .increment();
        }
    }

    /**
     * Métricas para manejo global de excepciones
     */
    @AfterReturning("execution(* *..*GlobalExceptionHandler.handleGeneralException(..))")
    public void afterExceptionHandled(JoinPoint jp) {
        registry.counter("loader.dinamico.exceptions.handled",
                        "servicio", "loader-dinamico",
                        "handler", "global")
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
                    "servicio", "loader-dinamico",
                    "class", className,
                    "method", method,
                    "result", "success"));

            // Registrar contador de éxitos
            incrementCounter(metricName + ".count",
                    "kind", kind,
                    "servicio", "loader-dinamico",
                    "class", className,
                    "method", method,
                    "result", "success");

            return result;

        } catch (Throwable t) {
            // Registrar timer de error
            sample.stop(getOrCreateTimer(metricName + ".seconds",
                    "kind", kind,
                    "servicio", "loader-dinamico",
                    "class", className,
                    "method", method,
                    "result", "error"));

            // Registrar contador de errores
            String exceptionName = t.getClass().getSimpleName();
            incrementCounter(metricName + ".errors",
                    "exception", exceptionName,
                    "kind", kind,
                    "servicio", "loader-dinamico",
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
}