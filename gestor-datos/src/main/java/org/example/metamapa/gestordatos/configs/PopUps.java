package org.example.metamapa.gestordatos.configs;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

@Component
public class PopUps implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Headers CRÍTICOS para permitir popups
        httpResponse.setHeader("Cross-Origin-Opener-Policy", "unsafe-none");
        httpResponse.setHeader("Cross-Origin-Embedder-Policy", "unsafe-none");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");

        chain.doFilter(request, response);
    }
}