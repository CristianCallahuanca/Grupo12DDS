package org.example.metamapa.loaderdemo.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.aspectj.lang.reflect.MethodSignature;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class DemoMetricsAspect {

    private final MeterRegistry registry;

    // Cache para performance
    private final Map<String, Timer> timerCache = new ConcurrentHashMap<>();
    private final Map<String, Counter> counterCache = new ConcurrentHashMap<>();

    // ==================== MÉTRICAS ESPECÍFICAS DEL LOADER DEMO ====================
    private final Counter fuentesDemoConsultadas;
    private final Counter hechosDemoObtenidos;
    private final Counter hechosDemoEnviados;
    private final Counter fuentesDemoRegistradas;
    private final Counter cargasProgramadasEjecutadas;
    private final Counter fuentesDemoOmitidas;

    // Gauges específicos
    private final AtomicInteger fuentesDemoActivas;
    private final AtomicInteger hechosDemoPendientes;

    public DemoMetricsAspect(MeterRegistry registry) {
        this.registry = registry;

        // Inicializar métricas ESPECÍFICAS del loader demo
        this.fuentesDemoConsultadas = Counter.builder("loader.demo.fuentes.consultadas")
                .description("Total de fuentes demo consultadas")
                .tag("servicio", "loader-demo")
                .tag("tipo_fuente", "demo")
                .register(registry);

        this.hechosDemoObtenidos = Counter.builder("loader.demo.hechos.obtenidos")
                .description("Total de hechos obtenidos de fuentes demo")
                .tag("servicio", "loader-demo")
                .tag("tipo", "obtencion")
                .register(registry);

        this.hechosDemoEnviados = Counter.builder("loader.demo.hechos.enviados")
                .description("Total de hechos enviados al agregador")
                .tag("servicio", "loader-demo")
                .tag("tipo", "envio")
                .register(registry);

        this.fuentesDemoRegistradas = Counter.builder("loader.demo.fuentes.registradas")
                .description("Fuentes demo registradas en el sistema")
                .tag("servicio", "loader-demo")
                .tag("operacion", "registro")
                .register(registry);

        this.cargasProgramadasEjecutadas = Counter.builder("loader.demo.cargas.programadas")
                .description("Cargas programadas ejecutadas")
                .tag("servicio", "loader-demo")
                .tag("tipo", "scheduled")
                .register(registry);

        this.fuentesDemoOmitidas = Counter.builder("loader.demo.fuentes.omitidas")
                .description("Fuentes demo omitidas por límite de tiempo")
                .tag("servicio", "loader-demo")
                .tag("razon", "frecuencia_consulta")
                .register(registry);

        // Gauges específicos
        this.fuentesDemoActivas = new AtomicInteger(0);
        registry.gauge("loader.demo.fuentes.activas",
                fuentesDemoActivas, AtomicInteger::get);

        this.hechosDemoPendientes = new AtomicInteger(0);
        registry.gauge("loader.demo.hechos.pendientes",
                hechosDemoPendientes, AtomicInteger::get);

        // Métricas adicionales específicas
        Counter.builder("loader.demo.parse.errors")
                .description("Errores de parseo de datos demo")
                .tag("servicio", "loader-demo")
                .tag("tipo", "data_parsing")
                .register(registry);

        Counter.builder("loader.demo.auth.errors")
                .description("Errores de autenticación con fuentes demo")
                .tag("servicio", "loader-demo")
                .tag("tipo", "authentication")
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

    // ==================== INTERCEPTORES ESPECÍFICOS DEL LOADER DEMO ====================

    @Around("execution(* *..*CargadorHechosService.*(..))")
    public Object aroundCargadorHechosService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "loader.demo.cargador.operations", "cargador_hechos");
    }

    @Around("execution(* *..*FuentesDemoService.*(..))")
    public Object aroundFuentesDemoService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "loader.demo.fuentes.operations", "fuentes_demo");
    }

    @Around("execution(* *..*HechosService.*(..))")
    public Object aroundHechosService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "loader.demo.hechos.operations", "hechos_service");
    }

    @Around("execution(* *..*RegistroFuenteService.*(..))")
    public Object aroundRegistroFuenteService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "loader.demo.registro.operations", "registro_fuente");
    }

    @Around("execution(* *..*AdapterFuenteDemo.*(..))")
    public Object aroundAdapterFuenteDemo(ProceedingJoinPoint pjp) throws Throwable {
        fuentesDemoConsultadas.increment();
        return recordExecution(pjp, "loader.demo.adapter.operations", "adapter_demo");
    }

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object aroundScheduledMethods(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();

        if ("ejecutarCargaProgramada".equals(methodName)) {
            cargasProgramadasEjecutadas.increment();

            registry.counter("loader.demo.scheduler.executions",
                            "servicio", "loader-demo",
                            "tarea", "carga_hechos",
                            "frecuencia", "30_segundos")
                    .increment();
        } else if ("retryRegistro".equals(methodName)) {
            registry.counter("loader.demo.registro.retry",
                            "servicio", "loader-demo",
                            "metodo", methodName)
                    .increment();
        }

        return recordExecution(pjp, "loader.demo.scheduled.tasks", "scheduled");
    }

    // ==================== MÉTRICAS ESPECÍFICAS DE NEGOCIO ====================

    /**
     * Métricas para cargarHechosDeTodasLasFuentes()
     */
    @AfterReturning("execution(* *..*CargadorHechosService.cargarHechosDeTodasLasFuentes(..))")
    public void afterCargarHechos(JoinPoint jp) {
        // Esta métrica se complementa con afterCargaFuenteIndividual
        registry.counter("loader.demo.carga.completada",
                        "servicio", "loader-demo",
                        "operacion", "carga_completa")
                .increment();
    }

    /**
     * Métricas para cada fuente consultada individualmente
     */
    @AfterReturning(
            pointcut = "execution(* *..*AdapterFuenteDemo.obtenerSiguienteHecho(..))",
            returning = "result"
    )
    public void afterCargaFuenteIndividual(JoinPoint jp, Object result) {
        if (result != null && result instanceof java.util.Optional) {
            @SuppressWarnings("unchecked")
            java.util.Optional<Map<String, Object>> optional = (java.util.Optional<Map<String, Object>>) result;
            if (optional.isPresent()) {
                hechosDemoObtenidos.increment();

                registry.counter("loader.demo.hecho.obtenido.individual",
                                "servicio", "loader-demo",
                                "resultado", "hecho_obtenido")
                        .increment();
            } else {
                registry.counter("loader.demo.hecho.obtenido.individual",
                                "servicio", "loader-demo",
                                "resultado", "sin_datos")
                        .increment();
            }
        }
    }

    /**
     * Métricas para fuentes omitidas por frecuencia
     */
    @After("execution(* *..*CargadorHechosService.cargarHechosDeTodasLasFuentes(..)) && " +
            "args() && " +
            "within(*..*CargadorHechosService)")
    public void trackFuentesOmitidas(JoinPoint jp) {
        // Esta métrica se incrementa en el propio método cuando se omite una fuente
        // pero podemos contar cuántas veces se ejecutó la lógica de omisión
    }

    /**
     * Métricas para errores de parseo de fecha
     */
    @AfterReturning(
            pointcut = "execution(* *..*CargadorHechosService.mapearADominio(..))",
            returning = "result"
    )
    public void afterMapeoHecho(JoinPoint jp, Object result) {
        registry.counter("loader.demo.hecho.mapeado",
                        "servicio", "loader-demo",
                        "operacion", "mapeo_dominio")
                .increment();
    }

    /**
     * Métricas para errores en parseo de fecha
     */
    @AfterThrowing(
            pointcut = "execution(* *..*CargadorHechosService.mapearADominio(..))",
            throwing = "ex"
    )
    public void afterErrorParseoFecha(JoinPoint jp, Exception ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("parsear")) {
            registry.counter("loader.demo.parse.errors",
                            "servicio", "loader-demo",
                            "campo", "fecha",
                            "accion", "usar_actual")
                    .increment();
        }
    }

    /**
     * Métricas para listarHechos() - envío al agregador
     */
    @AfterReturning(
            pointcut = "execution(* *..*HechosService.listarHechos(..))",
            returning = "result"
    )
    public void afterListarHechos(JoinPoint jp, Object result) {
        if (result instanceof List<?> list) {
            int cantidad = list.size();
            hechosDemoEnviados.increment(cantidad);

            // Actualizar gauge de hechos pendientes
            hechosDemoPendientes.set(Math.max(0, hechosDemoPendientes.get() - cantidad));

            registry.counter("loader.demo.hechos.lote.enviado",
                            "servicio", "loader-demo",
                            "cantidad", String.valueOf(cantidad),
                            "estado", "marcado_enviado")
                    .increment();

            if (cantidad == 0) {
                registry.counter("loader.demo.hechos.lote.vacio",
                                "servicio", "loader-demo",
                                "razon", "sin_hechos_pendientes")
                        .increment();
            }
        }
    }

    /**
     * Métricas para registrarFuenteDemo()
     */
    @AfterReturning(
            pointcut = "execution(* *..*FuentesDemoService.registrarFuenteDemo(..))",
            returning = "result"
    )
    public void afterRegistrarFuenteDemo(JoinPoint jp, Object result) {
        if (result != null) {
            fuentesDemoRegistradas.increment();
            fuentesDemoActivas.incrementAndGet();

            registry.counter("loader.demo.fuente.nueva",
                            "servicio", "loader-demo",
                            "operacion", "registro_completo")
                    .increment();
        }
    }

    /**
     * Métricas para listarFuentesDemo()
     */
    @AfterReturning(
            pointcut = "execution(* *..*FuentesDemoService.listarFuentesDemo(..))",
            returning = "result"
    )
    public void afterListarFuentesDemo(JoinPoint jp, Object result) {
        if (result instanceof List<?> list) {
            fuentesDemoActivas.set(list.size());

            registry.counter("loader.demo.fuentes.listadas",
                            "servicio", "loader-demo",
                            "cantidad", String.valueOf(list.size()))
                    .increment();
        }
    }

    /**
     * Métricas para obtenerFuentesActivas()
     */
    @AfterReturning(
            pointcut = "execution(* *..*FuentesDemoService.obtenerFuentesActivas(..))",
            returning = "result"
    )
    public void afterObtenerFuentesActivas(JoinPoint jp, Object result) {
        if (result instanceof List<?> list) {
            registry.counter("loader.demo.fuentes.activas.consultadas",
                            "servicio", "loader-demo",
                            "cantidad", String.valueOf(list.size()),
                            "uso", "carga_programada")
                    .increment();
        }
    }

    /**
     * Métricas para registro exitoso en el agregador
     */
    @AfterReturning("execution(* *..*RegistroFuenteService.intentarRegistro(..))")
    public void afterRegistroExitoso(JoinPoint jp) {
        registry.counter("loader.demo.registro.agregador.exitoso",
                        "servicio", "loader-demo",
                        "origen", getArgOrDefault(jp, 0, "desconocido"),
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
        registry.counter("loader.demo.registro.agregador.fallido",
                        "servicio", "loader-demo",
                        "exception", ex.getClass().getSimpleName(),
                        "origen", getArgOrDefault(jp, 0, "desconocido"))
                .increment();
    }

    /**
     * Métricas para ScheduledCargarHechos
     */
    @AfterReturning("execution(* *..*ScheduledCargarHechos.ejecutarCargaProgramada(..))")
    public void afterCargaProgramada(JoinPoint jp) {
        registry.counter("loader.demo.scheduler.completado",
                        "servicio", "loader-demo",
                        "tarea", "carga_hechos_demo")
                .increment();
    }

    /**
     * Métricas para fuentes omitidas (cooldown)
     */
    @After("execution(* *..*CargadorHechosService.cargarHechosDeTodasLasFuentes(..))")
    public void trackCooldownFuentes(JoinPoint jp) {
        // El propio servicio ya loguea cuando omite una fuente
        // Podemos contar las ejecuciones donde se evaluaron fuentes
        registry.counter("loader.demo.carga.evaluacion",
                        "servicio", "loader-demo",
                        "accion", "verificacion_cooldown")
                .increment();
    }

    /**
     * Métricas para errores en consulta de fuentes
     */
    @AfterThrowing(
            pointcut = "execution(* *..*CargadorHechosService.cargarHechosDeTodasLasFuentes(..))",
            throwing = "ex"
    )
    public void handleErrorCargaFuente(JoinPoint jp, Exception ex) {
        registry.counter("loader.demo.carga.error",
                        "servicio", "loader-demo",
                        "exception", ex.getClass().getSimpleName(),
                        "contexto", "carga_fuentes_demo")
                .increment();
    }

    /**
     * Métricas para mapeo exitoso de DTOs
     */
    @AfterReturning(
            pointcut = "execution(* *..*HechosService.mapearADTO(..))",
            returning = "result"
    )
    public void afterMapeoDTOExitoso(JoinPoint jp, Object result) {
        registry.counter("loader.demo.dto.mapeado",
                        "servicio", "loader-demo",
                        "tipo", "hecho_a_dto",
                        "resultado", "exitoso")
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
                    "servicio", "loader-demo",
                    "class", className,
                    "method", method,
                    "result", "success"));

            // Registrar contador de éxitos
            incrementCounter(metricName + ".count",
                    "kind", kind,
                    "servicio", "loader-demo",
                    "class", className,
                    "method", method,
                    "result", "success");

            return result;

        } catch (Throwable t) {
            // Registrar timer de error
            sample.stop(getOrCreateTimer(metricName + ".seconds",
                    "kind", kind,
                    "servicio", "loader-demo",
                    "class", className,
                    "method", method,
                    "result", "error"));

            // Registrar contador de errores
            String exceptionName = t.getClass().getSimpleName();
            incrementCounter(metricName + ".errors",
                    "exception", exceptionName,
                    "kind", kind,
                    "servicio", "loader-demo",
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
}