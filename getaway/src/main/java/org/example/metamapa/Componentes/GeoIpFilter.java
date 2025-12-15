package org.example.metamapa.Componentes;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.metamapa.Servicios.GeoIpService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GeoIpFilter extends OncePerRequestFilter {

    private final GeoIpService geoIpService;

    public GeoIpFilter(GeoIpService geoIpService) {
        this.geoIpService = geoIpService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = getClientIp(request);

        // Permitir localhost y redes privadas (DEV)
        if (esIpLocal(clientIp)) {
            filterChain.doFilter(request, response);
            return;
        }

        String countryCode = geoIpService.getCountryCode(clientIp);

        System.out.println("Conexión desde IP: " + clientIp + " | País: " + countryCode);

        if (!"AR".equalsIgnoreCase(countryCode)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Acceso permitido solo desde Argentina");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    private boolean esIpLocal(String ip) {
        return ip.equals("127.0.0.1")
                || ip.equals("0:0:0:0:0:0:0:1")
                || ip.startsWith("192.168.")
                || ip.startsWith("10.")
                || ip.startsWith("172.");
    }
}
