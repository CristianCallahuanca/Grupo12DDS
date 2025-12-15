package org.example.metamapa.gestordatos.Metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class GestorMetricsAspect {

    private final MeterRegistry registry;

    // Cache para performance
    private final Map<String, Timer> timerCache = new ConcurrentHashMap<>();
    private final Map<String, Counter> counterCache = new ConcurrentHashMap<>();

    // ==================== MÉTRICAS ESPECÍFICAS DEL GESTOR ====================
    private final Counter coleccionesCreadas;
    private final Counter hechosEditados;
    private final Counter solicitudesGestionadas;
    private final Counter usuariosRegistrados;
    private final Counter loginsExitosos;
    private final Counter loginsGoogle;
    private final Counter filtrosAplicados;

    // Gauges específicos
    private final AtomicInteger coleccionesActivas;
    private final AtomicInteger solicitudesPendientes;
    private final AtomicInteger hechosEnRevision;
    private final AtomicInteger usuariosActivos;

    public GestorMetricsAspect(MeterRegistry registry) {
        this.registry = registry;

        // Inicializar métricas ESPECÍFICAS del gestor
        this.coleccionesCreadas = Counter.builder("gestor.colecciones.creadas")
                .description("Total de colecciones creadas")
                .tag("servicio", "gestor")
                .register(registry);

        this.hechosEditados = Counter.builder("gestor.hechos.editados")
                .description("Hechos editados por contribuyentes")
                .tag("servicio", "gestor")
                .tag("tipo", "edicion_controlada")
                .register(registry);

        this.solicitudesGestionadas = Counter.builder("gestor.solicitudes.gestionadas")
                .description("Solicitudes de eliminación gestionadas")
                .tag("servicio", "gestor")
                .register(registry);

        this.usuariosRegistrados = Counter.builder("gestor.usuarios.registrados")
                .description("Usuarios registrados en el sistema")
                .tag("servicio", "gestor")
                .register(registry);

        this.loginsExitosos = Counter.builder("gestor.auth.logins.exitosos")
                .description("Logins exitosos")
                .tag("servicio", "gestor")
                .tag("provider", "local")
                .register(registry);

        this.loginsGoogle = Counter.builder("gestor.auth.logins.google")
                .description("Logins exitosos con Google")
                .tag("servicio", "gestor")
                .tag("provider", "google")
                .register(registry);

        this.filtrosAplicados = Counter.builder("gestor.filtros.aplicados")
                .description("Filtros aplicados a hechos")
                .tag("servicio", "gestor")
                .register(registry);

        // Gauges específicos
        this.coleccionesActivas = new AtomicInteger(0);
        registry.gauge("gestor.colecciones.activas",
                coleccionesActivas, AtomicInteger::get);

        this.solicitudesPendientes = new AtomicInteger(0);
        registry.gauge("gestor.solicitudes.pendientes",
                solicitudesPendientes, AtomicInteger::get);

        this.hechosEnRevision = new AtomicInteger(0);
        registry.gauge("gestor.hechos.en_revision",
                hechosEnRevision, AtomicInteger::get);

        this.usuariosActivos = new AtomicInteger(0);
        registry.gauge("gestor.usuarios.activos",
                usuariosActivos, AtomicInteger::get);

        // Métricas adicionales específicas
        Counter.builder("gestor.consenso.aplicado")
                .description("Consensos aplicados a colecciones")
                .tag("servicio", "gestor")
                .tag("tipo", "automatico")
                .register(registry);

        Counter.builder("gestor.oauth.google.exchanges")
                .description("Intercambios de código OAuth con Google")
                .tag("servicio", "gestor")
                .register(registry);

        Counter.builder("gestor.ediciones.rechazadas")
                .description("Ediciones de hechos rechazadas")
                .tag("servicio", "gestor")
                .tag("razon", "plazo_expirado")
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

    // ==================== INTERCEPTORES ESPECÍFICOS DEL GESTOR ====================

    @Around("execution(* *..*ColeccionesService.*(..))")
    public Object aroundColeccionesService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "gestor.colecciones.operations", "colecciones");
    }

    @Around("execution(* *..*HechoService.*(..))")
    public Object aroundHechoService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "gestor.hechos.operations", "hechos");
    }

    @Around("execution(* *..*SolicitudesService.*(..))")
    public Object aroundSolicitudesService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "gestor.solicitudes.operations", "solicitudes");
    }

    @Around("execution(* *..*ContribuyenteService.*(..))")
    public Object aroundContribuyenteService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "gestor.auth.operations", "auth");
    }

    @Around("execution(* *..*GoogleAuthService.*(..))")
    public Object aroundGoogleAuthService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "gestor.oauth.operations", "oauth");
    }

    @Around("execution(* *..*FiltradorService.*(..))")
    public Object aroundFiltradorService(ProceedingJoinPoint pjp) throws Throwable {
        return recordExecution(pjp, "gestor.filtros.operations", "filtros");
    }

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object aroundScheduledMethods(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();

        if ("ejecutarConsensoDiario".equals(methodName)) {
            registry.counter("gestor.consenso.scheduled",
                            "servicio", "gestor",
                            "tarea", "consenso_diario",
                            "hora", "03:00")
                    .increment();
        }

        return recordExecution(pjp, "gestor.scheduled.tasks", "scheduled");
    }

    // ==================== MÉTRICAS ESPECÍFICAS DE NEGOCIO ====================

    /**
     * Métricas para crearColeccion()
     */
    @AfterReturning(
            pointcut = "execution(* *..*ColeccionesService.crearColeccion(..))",
            returning = "result"
    )
    public void afterColeccionCreada(JoinPoint jp, Object result) {
        coleccionesCreadas.increment();
        coleccionesActivas.incrementAndGet();

        registry.counter("gestor.coleccion.operacion",
                        "servicio", "gestor",
                        "tipo", "creacion",
                        "resultado", "exitoso")
                .increment();
    }

    /**
     * Métricas para listarColecciones()
     */
    @AfterReturning(
            pointcut = "execution(* *..*ColeccionesService.listarColecciones(..))",
            returning = "result"
    )
    public void afterListarColecciones(JoinPoint jp, Object result) {
        if (result instanceof List<?> list) {
            coleccionesActivas.set(list.size());

            registry.counter("gestor.coleccion.operacion",
                            "servicio", "gestor",
                            "tipo", "listado",
                            "cantidad", String.valueOf(list.size()))
                    .increment();
        }
    }

    /**
     * Métricas para actualizarColeccion()
     */
    @AfterReturning(
            pointcut = "execution(* *..*ColeccionesService.actualizarColeccion(..))",
            returning = "result"
    )
    public void afterActualizarColeccion(JoinPoint jp, Object result) {
        if (result instanceof Boolean success && success) {
            registry.counter("gestor.coleccion.operacion",
                            "servicio", "gestor",
                            "tipo", "actualizacion",
                            "resultado", "exitoso")
                    .increment();
        }
    }

    /**
     * Métricas para aplicarConsensoATodas()
     */
    @AfterReturning("execution(* *..*ColeccionesService.aplicarConsensoATodas(..))")
    public void afterConsensoAplicado(JoinPoint jp) {
        registry.counter("gestor.consenso.aplicado",
                        "servicio", "gestor",
                        "tipo", "manual",
                        "alcance", "todas_colecciones")
                .increment();
    }

    /**
     * Métricas para crearSolicitudEliminacion()
     */
    @AfterReturning(
            pointcut = "execution(* *..*SolicitudesService.crearSolicitudEliminacion(..))",
            returning = "result"
    )
    public void afterSolicitudCreada(JoinPoint jp, Object result) {
        solicitudesGestionadas.increment();
        solicitudesPendientes.incrementAndGet();

        Object[] args = jp.getArgs();
        if (args.length > 0) {
            registry.counter("gestor.solicitud.operacion",
                            "servicio", "gestor",
                            "tipo", "creacion",
                            "estado", "pendiente")
                    .increment();
        }
    }

    /**
     * Métricas para aprobarSolicitud()
     */
    @AfterReturning(
            pointcut = "execution(* *..*SolicitudesService.aprobarSolicitud(..))",
            returning = "result"
    )
    public void afterSolicitudAprobada(JoinPoint jp, Object result) {
        if (result != null) {
            solicitudesGestionadas.increment();
            solicitudesPendientes.decrementAndGet();

            registry.counter("gestor.solicitud.operacion",
                            "servicio", "gestor",
                            "tipo", "aprobacion",
                            "estado", "aprobada")
                    .increment();
        }
    }

    /**
     * Métricas para denegarSolicitud()
     */
    @AfterReturning(
            pointcut = "execution(* *..*SolicitudesService.denegarSolicitud(..))",
            returning = "result"
    )
    public void afterSolicitudDenegada(JoinPoint jp, Object result) {
        if (result != null) {
            solicitudesGestionadas.increment();
            solicitudesPendientes.decrementAndGet();

            registry.counter("gestor.solicitud.operacion",
                            "servicio", "gestor",
                            "tipo", "denegacion",
                            "estado", "rechazada")
                    .increment();
        }
    }

    /**
     * Métricas para listarSolicitudesPendientes()
     */
    @AfterReturning(
            pointcut = "execution(* *..*SolicitudesService.listarSolicitudesPendientes(..))",
            returning = "result"
    )
    public void afterListarSolicitudes(JoinPoint jp, Object result) {
        if (result instanceof List<?> list) {
            solicitudesPendientes.set(list.size());

            registry.counter("gestor.solicitud.operacion",
                            "servicio", "gestor",
                            "tipo", "consulta",
                            "cantidad", String.valueOf(list.size()))
                    .increment();
        }
    }

    /**
     * Métricas para crearContribuyenteRegistrado()
     */
    @AfterReturning(
            pointcut = "execution(* *..*ContribuyenteService.crearContribuyenteRegistrado(..))",
            returning = "result"
    )
    public void afterUsuarioRegistrado(JoinPoint jp, Object result) {
        usuariosRegistrados.increment();
        usuariosActivos.incrementAndGet();

        registry.counter("gestor.auth.operacion",
                        "servicio", "gestor",
                        "tipo", "registro",
                        "provider", "local",
                        "resultado", "exitoso")
                .increment();
    }

    /**
     * Métricas para login()
     */
    @AfterReturning(
            pointcut = "execution(* *..*ContribuyenteService.login(..))",
            returning = "result"
    )
    public void afterLoginExitoso(JoinPoint jp, Object result) {
        loginsExitosos.increment();

        registry.counter("gestor.auth.operacion",
                        "servicio", "gestor",
                        "tipo", "login",
                        "provider", "local",
                        "resultado", "exitoso")
                .increment();
    }

    /**
     * Métricas para loginConGoogle()
     */
    @AfterReturning(
            pointcut = "execution(* *..*ContribuyenteService.loginConGoogle(..))",
            returning = "result"
    )
    public void afterLoginGoogleExitoso(JoinPoint jp, Object result) {
        loginsGoogle.increment();

        registry.counter("gestor.auth.operacion",
                        "servicio", "gestor",
                        "tipo", "login",
                        "provider", "google",
                        "resultado", "exitoso")
                .increment();
    }

    /**
     * Métricas para exchangeCodeForTokens()
     */
    @AfterReturning(
            pointcut = "execution(* *..*GoogleAuthService.exchangeCodeForTokens(..))",
            returning = "result"
    )
    public void afterOAuthExchange(JoinPoint jp, Object result) {
        registry.counter("gestor.oauth.google.exchanges",
                        "servicio", "gestor",
                        "operacion", "code_for_tokens",
                        "resultado", "exitoso")
                .increment();
    }

    /**
     * Métricas para decodeGoogleToken()
     */
    @AfterReturning(
            pointcut = "execution(* *..*GoogleAuthService.decodeGoogleToken(..))",
            returning = "result"
    )
    public void afterTokenDecoded(JoinPoint jp, Object result) {
        registry.counter("gestor.oauth.token.decoded",
                        "servicio", "gestor",
                        "tipo", "id_token",
                        "resultado", "exitoso")
                .increment();
    }

    /**
     * Métricas para editarHechoContribuyente()
     */
    @AfterReturning(
            pointcut = "execution(* *..*HechoService.editarHechoContribuyente(..))",
            returning = "result"
    )
    public void afterHechoEditado(JoinPoint jp, Object result) {
        if (result instanceof Boolean success && success) {
            hechosEditados.increment();

            registry.counter("gestor.hecho.operacion",
                            "servicio", "gestor",
                            "tipo", "edicion",
                            "autor", "contribuyente",
                            "resultado", "exitoso")
                    .increment();
        }
    }

    /**
     * Métricas para errores de edición fuera de plazo
     */
    @AfterThrowing(
            pointcut = "execution(* *..*HechoService.editarHechoContribuyente(..))",
            throwing = "ex"
    )
    public void afterEdicionRechazada(JoinPoint jp, Exception ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("plazo")) {
            registry.counter("gestor.ediciones.rechazadas",
                            "servicio", "gestor",
                            "razon", "plazo_expirado")
                    .increment();
        }
    }

    /**
     * Métricas para aprobar/denegar solicitudes de hecho
     */
    @AfterReturning(
            pointcut = "execution(* *..*HechoService.aprobarSolicitud(..)) || " +
                    "execution(* *..*HechoService.denegarSolicitud(..))",
            returning = "result"
    )
    public void afterSolicitudHechoGestionada(JoinPoint jp, Object result) {
        if (result != null) {
            String methodName = getMethodName(jp);
            String accion = methodName.contains("aprobar") ? "aprobacion" : "denegacion";

            registry.counter("gestor.hecho.operacion",
                            "servicio", "gestor",
                            "tipo", "gestion_solicitud",
                            "accion", accion)
                    .increment();
        }
    }

    /**
     * Métricas para filtrarHechosDataBase()
     */
    @AfterReturning(
            pointcut = "execution(* *..*FiltradorService.filtrarHechosDataBase(..))",
            returning = "result"
    )
    public void afterFiltradoHechos(JoinPoint jp, Object result) {
        filtrosAplicados.increment();

        if (result instanceof List<?> list) {
            Object[] args = jp.getArgs();
            int condiciones = args.length > 0 && args[0] instanceof List ? ((List<?>) args[0]).size() : 0;

            registry.counter("gestor.filtro.ejecutado",
                            "servicio", "gestor",
                            "condiciones", String.valueOf(condiciones),
                            "resultados", String.valueOf(list.size()))
                    .increment();

            // Histograma de resultados por filtro
            registry.summary("gestor.filtro.resultados.tamano",
                            "servicio", "gestor")
                    .record(list.size());
        }
    }

    /**
     * Métricas para buscarTodosLosHechos()
     */
    @AfterReturning(
            pointcut = "execution(* *..*HechoService.buscarTodosLosHechos(..))",
            returning = "result"
    )
    public void afterBusquedaHechos(JoinPoint jp, Object result) {
        if (result instanceof List<?> list) {
            registry.counter("gestor.hecho.operacion",
                            "servicio", "gestor",
                            "tipo", "busqueda",
                            "cantidad_resultados", String.valueOf(list.size()))
                    .increment();
        }
    }

    /**
     * Métricas para scheduler de consenso
     */
    @AfterReturning("execution(* *..*ConsensoScheduler.ejecutarConsensoDiario(..))")
    public void afterConsensoScheduled(JoinPoint jp) {
        registry.counter("gestor.consenso.scheduled.completado",
                        "servicio", "gestor",
                        "tarea", "consenso_diario",
                        "hora", "03:00")
                .increment();
    }

    /**
     * Métricas para errores de autenticación
     */
    @AfterThrowing(
            pointcut = "execution(* *..*ContribuyenteService.login(..)) || " +
                    "execution(* *..*ContribuyenteService.loginConGoogle(..))",
            throwing = "ex"
    )
    public void afterAuthError(JoinPoint jp, Exception ex) {
        String provider = getMethodName(jp).contains("Google") ? "google" : "local";

        registry.counter("gestor.auth.error",
                        "servicio", "gestor",
                        "provider", provider,
                        "exception", ex.getClass().getSimpleName(),
                        "razon", extraerRazonError(ex))
                .increment();
    }

    /**
     * Métricas para emails duplicados en registro
     */
    @AfterThrowing(
            pointcut = "execution(* *..*ContribuyenteService.crearContribuyenteRegistrado(..))",
            throwing = "ex"
    )
    public void afterRegistroError(JoinPoint jp, Exception ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("ya está registrado")) {
            registry.counter("gestor.auth.operacion",
                            "servicio", "gestor",
                            "tipo", "registro",
                            "resultado", "email_duplicado")
                    .increment();
        }
    }

    /**
     * Métricas para detección de spam en solicitudes
     */
    @After("execution(* *..*SolicitudesService.crearSolicitudEliminacion(..)) && args(dto)")
    public void afterSpamDetection(JoinPoint jp, Object dto) {
        // La detección de spam ocurre dentro del método
        registry.counter("gestor.solicitud.spam.verificado",
                        "servicio", "gestor")
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
                    "servicio", "gestor",
                    "class", className,
                    "method", method,
                    "result", "success"));

            // Registrar contador de éxitos
            incrementCounter(metricName + ".count",
                    "kind", kind,
                    "servicio", "gestor",
                    "class", className,
                    "method", method,
                    "result", "success");

            return result;

        } catch (Throwable t) {
            // Registrar timer de error
            sample.stop(getOrCreateTimer(metricName + ".seconds",
                    "kind", kind,
                    "servicio", "gestor",
                    "class", className,
                    "method", method,
                    "result", "error"));

            // Registrar contador de errores
            String exceptionName = t.getClass().getSimpleName();
            incrementCounter(metricName + ".errors",
                    "exception", exceptionName,
                    "kind", kind,
                    "servicio", "gestor",
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

    private String extraerRazonError(Exception ex) {
        String message = ex.getMessage();
        if (message == null) return "desconocido";

        if (message.contains("credenciales") || message.contains("contraseña")) return "credenciales_invalidas";
        if (message.contains("no encontrado")) return "usuario_no_existe";
        if (message.contains("registrado")) return "email_duplicado";
        if (message.contains("token") || message.contains("JWT")) return "token_invalido";

        return "general";
    }
}