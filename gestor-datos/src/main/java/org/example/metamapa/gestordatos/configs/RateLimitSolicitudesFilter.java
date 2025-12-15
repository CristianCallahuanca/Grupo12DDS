package org.example.metamapa.gestordatos.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitSolicitudesFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /* =========================
       CREACIÓN DE BUCKETS
       ========================= */

    private Bucket crearBucketUsuario() {
        return Bucket4j.builder()
                .addLimit(
                        Bandwidth.classic(
                                2,
                                Refill.intervally(2, Duration.ofHours(1))
                        )
                )
                .build();
    }

    private Bucket crearBucketIp() {
        return Bucket4j.builder()
                .addLimit(
                        Bandwidth.classic(
                                2,
                                Refill.intervally(2, Duration.ofHours(1))
                        )
                )
                .build();
    }

    private Bucket bucketPorClave(String clave) {
        return buckets.computeIfAbsent(clave, k -> {
            if (k.startsWith("USER:")) {
                return crearBucketUsuario();
            }
            return crearBucketIp();
        });
    }

    /* =========================
       UTILIDADES
       ========================= */

    private String obtenerIp(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) {
            return ip.split(",")[0];
        }
        return req.getRemoteAddr();
    }

    /* =========================
       FILTRO
       ========================= */

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        MultiReadHttpServletRequest wrapped =
                new MultiReadHttpServletRequest((HttpServletRequest) request);

        HttpServletResponse res = (HttpServletResponse) response;

        if ("POST".equalsIgnoreCase(wrapped.getMethod())
                && wrapped.getRequestURI().endsWith("/gestordatos/publica/solicitudes")) {

            String body = wrapped.getBodyString();

            if (body != null && !body.isBlank()) {

                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> json = mapper.readValue(body, Map.class);

                String claveRateLimit;

                if (json.containsKey("usuarioId") && json.get("usuarioId") != null) {
                    // Usuario registrado
                    claveRateLimit = "USER:" + json.get("usuarioId").toString();
                } else {
                    // Usuario no registrado → IP
                    String ip = obtenerIp(wrapped);
                    claveRateLimit = "IP:" + ip;
                }

                Bucket bucket = bucketPorClave(claveRateLimit);

                if (!bucket.tryConsume(1)) {
                    res.setStatus(429);
                    res.getWriter().write("Has superado el limite permitido de solicitudes.");
                    return;
                }
            }
        }

        chain.doFilter(wrapped, response);
    }
}
