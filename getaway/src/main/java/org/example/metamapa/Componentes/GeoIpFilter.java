package org.example.metamapa.Componentes;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.metamapa.Servicios.GeoIpService;
import org.example.metamapa.Servicios.IDireccionesIpService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GeoIpFilter extends OncePerRequestFilter {

    private final GeoIpService geoIpService;
    private final IDireccionesIpService direccionesIPService;

    public GeoIpFilter(
            GeoIpService geoIpService,
            IDireccionesIpService direccionesIPService
    ) {
        this.geoIpService = geoIpService;
        this.direccionesIPService = direccionesIPService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = getClientIp(request);

        // 1️⃣ BLOQUEO POR IP EN DB (PRIORIDAD MÁXIMA)
        if (direccionesIPService.ipBloqueada(clientIp)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("IP bloqueada por el administrador");
            return;
        }

        // 2️⃣ Permitir localhost y redes privadas
        if (esIpLocal(clientIp)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3️⃣ Bloqueo por país
        String countryCode = geoIpService.getCountryCode(clientIp);

        System.out.println("IP: " + clientIp + " | País: " + countryCode);

        if (!"AR".equalsIgnoreCase(countryCode)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Acceso permitido solo desde Argentina");
            return;
        }

        // 4️⃣ OK
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
