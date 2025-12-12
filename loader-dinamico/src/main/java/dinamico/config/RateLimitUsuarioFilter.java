package dinamico.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RateLimitUsuarioFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket crearBucket() {
        return Bucket4j.builder()
                .addLimit(
                        Bandwidth.classic(
                                5,                                          // máximo 5
                                Refill.intervally(5, Duration.ofHours(1))  // cada 1h
                        )
                )
                .build();
    }

    private Bucket bucketPorUsuario(String usuario) {
        return buckets.computeIfAbsent(usuario, k -> crearBucket());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        System.out.println(">>> Filtro ejecutadooooooo");
        System.out.println("URI REAL = " + req.getRequestURI());
        System.out.println("ServletPath = " + req.getServletPath());
        System.out.println("Metodo = " + req.getMethod());

        // SOLO aplicar al POST /hecho
        if ("POST".equals(req.getMethod()) &&
                "/fuenteDinamica/hecho".equals(req.getServletPath())) {

            // Leer JSON "data" del multipart
            String json = req.getParameter("data");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(json, Map.class);

            // Usuario real del sistema
            String usuarioId = map.get("contribuyenteID").toString();

            Bucket bucket = bucketPorUsuario(usuarioId);

            if (!bucket.tryConsume(1)) {
                HttpServletResponse res = (HttpServletResponse) response;
                res.setStatus(429);
                res.getWriter().write("Has superado el limite permitido.");
                return;
            }

            System.out.println("Tokens restantes usuario " + usuarioId + ": " + bucket.getAvailableTokens());
        }

        chain.doFilter(request, response);
    }
}
