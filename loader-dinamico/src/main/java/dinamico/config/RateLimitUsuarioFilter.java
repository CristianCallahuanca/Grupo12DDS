package dinamico.config;

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

public class RateLimitUsuarioFilter implements Filter {

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

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Solo aplicar al POST /fuenteDinamica/hecho
        if ("POST".equalsIgnoreCase(req.getMethod())
                && "/fuenteDinamica/hecho".equals(req.getServletPath())) {

            // Leer JSON "data" del multipart
            String json = req.getParameter("data");

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(json, Map.class);

            String claveRateLimit;

            if (map.containsKey("contribuyenteID") && map.get("contribuyenteID") != null) {
                // Usuario registrado
                claveRateLimit = "USER:" + map.get("contribuyenteID").toString();
            } else {
                // Usuario anónimo → IP
                String ip = obtenerIp(req);
                claveRateLimit = "IP:" + ip;
            }

            Bucket bucket = bucketPorClave(claveRateLimit);

            if (!bucket.tryConsume(1)) {
                res.setStatus(429);
                res.getWriter().write("Superaste la cantidad maxima intanta mas tarde");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
