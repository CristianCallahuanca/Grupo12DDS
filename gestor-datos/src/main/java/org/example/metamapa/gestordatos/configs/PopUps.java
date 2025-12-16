package org.example.metamapa.gestordatos.configs;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1) // Ejecutar antes del CorsFilter
public class PopUps implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();

        // Solo aplicar headers especiales a rutas de Google OAuth
        if (path.contains("/google/callback")) {
            // Headers específicos para popups
            httpResponse.setHeader("Cross-Origin-Opener-Policy", "unsafe-none");
            httpResponse.setHeader("Cross-Origin-Embedder-Policy", "unsafe-none");
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        }

        chain.doFilter(request, response);
    }
}