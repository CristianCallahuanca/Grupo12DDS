package org.example.metamapa.gestordatos.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitSolicitudesFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket crearBucket() {
        return Bucket4j.builder()
                .addLimit(Bandwidth.classic(
                        3,
                        Refill.intervally(3, Duration.ofHours(1))
                ))
                .build();
    }

    private Bucket bucketPorUsuario(String usuario) {
        return buckets.computeIfAbsent(usuario, k -> crearBucket());
    }

    private String leerBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        MultiReadHttpServletRequest wrapped = new MultiReadHttpServletRequest((HttpServletRequest) request);
        HttpServletResponse res = (HttpServletResponse) response;

        System.out.println("ENTRO AL FILTER");
        System.out.println("getRequestURI(): " + wrapped.getRequestURI());

        if (wrapped.getMethod().equals("POST") &&
                wrapped.getRequestURI().endsWith("/gestordatos/publica/solicitudes")) {

            System.out.println("ENTRO AL IF");

            String body = wrapped.getBodyString();
            System.out.println("BODY EN FILTER: " + body);

            if (body == null || body.isBlank()) {
                System.out.println("BODY VACÍO — se continúa igual");
                chain.doFilter(wrapped, response);
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> json = mapper.readValue(body, Map.class);

            String usuarioId = json.get("usuarioId").toString();

            Bucket bucket = bucketPorUsuario(usuarioId);

            if (!bucket.tryConsume(1)) {
                res.setStatus(429);
                res.getWriter().write("Has superado el limite permitido de solicitudes.");
                return;
            }

            System.out.println("Tokens restantes: " + bucket.getAvailableTokens());
        }

        chain.doFilter(wrapped, response);
    }
}
