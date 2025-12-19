package org.example.metamapa.Controladores;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.example.metamapa.Config.GatewayProperties;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

@RestController
@Slf4j
public class GatewayController {

    private final Map<String, String> servicios;

    public GatewayController(GatewayProperties props) {
        this.servicios = props.getServicios();
    }

    @RequestMapping("/{modulo}/**")
    public void proxy(HttpServletRequest request,
                      HttpServletResponse response,
                      @PathVariable String modulo) throws IOException {

        String baseUrl = servicios.get(modulo);
        if (baseUrl == null) {
            response.sendError(400, "Módulo desconocido: " + modulo);
            return;
        }

        String path = request.getRequestURI().substring(("/" + modulo).length());

        String targetUrl = baseUrl + "/" + modulo + path
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        log.info("Proxy {} {} -> {}", request.getMethod(), request.getRequestURI(), targetUrl);

        // ====== CASO ESPECIAL: OAuth Google (NO proxyear HTML) ======
        String fullPath = request.getRequestURI(); // ej: /gestordatos/contribuyentes/google
        if ("/gestordatos/contribuyentes/google".equals(fullPath)) {

            // Solo GET permitido
            if (!"GET".equalsIgnoreCase(request.getMethod())) {
                response.sendError(405, "Method not allowed");
                return;
            }

            // Forzamos llamar al gestor directamente para obtener el 302 Location
            String redirectBaseUrl = servicios.get("gestordatos");
            if (redirectBaseUrl == null) {
                response.sendError(500, "Servicio 'gestordatos' no configurado en gateway.servicios");
                return;
            }

            String redirectTargetUrl = redirectBaseUrl + fullPath;

            try (CloseableHttpClient client = HttpClients.createDefault();
                 CloseableHttpResponse proxyResp = client.execute(new HttpGet(redirectTargetUrl))) {

                int code = proxyResp.getCode();
                response.setStatus(code);

                // Copiar Location si existe (clave para que el browser siga el redirect a Google)
                Header loc = proxyResp.getFirstHeader("Location");
                if (loc != null) {
                    response.setHeader("Location", loc.getValue());
                }

                // (Opcional) Copiar cache headers por prolijidad
                Header cacheControl = proxyResp.getFirstHeader("Cache-Control");
                if (cacheControl != null) response.setHeader("Cache-Control", cacheControl.getValue());

                Header pragma = proxyResp.getFirstHeader("Pragma");
                if (pragma != null) response.setHeader("Pragma", pragma.getValue());

                return;
            }
        }

        // ====== PROXY NORMAL ======
        HttpUriRequest proxyRequest;

        switch (request.getMethod()) {
            case "POST" -> proxyRequest = withBody(new HttpPost(targetUrl), request);
            case "PUT" -> proxyRequest = withBody(new HttpPut(targetUrl), request);
            case "PATCH" -> proxyRequest = withBody(new HttpPatch(targetUrl), request);
            case "DELETE" -> proxyRequest = new HttpDelete(targetUrl);
            case "GET" -> proxyRequest = new HttpGet(targetUrl);

            case "OPTIONS" -> {
                response.setStatus(204);
                return;
            }

            default -> {
                response.sendError(405, "Método no soportado: " + request.getMethod());
                return;
            }
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!headerName.equalsIgnoreCase("host") && !headerName.equalsIgnoreCase("content-length")) {
                proxyRequest.addHeader(headerName, request.getHeader(headerName));
            }
        }

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse proxyResponse = client.execute(proxyRequest)) {

            response.setStatus(proxyResponse.getCode());

            for (Header header : proxyResponse.getHeaders()) {
                if (!header.getName().equalsIgnoreCase("transfer-encoding")) {
                    response.setHeader(header.getName(), header.getValue());
                }
            }

            if (proxyResponse.getEntity() != null) {
                proxyResponse.getEntity().getContent().transferTo(response.getOutputStream());
            }
        }
    }

    private HttpUriRequest withBody(HttpUriRequestBase req, HttpServletRequest request) throws IOException {
        ContentType ct = request.getContentType() != null
                ? ContentType.parse(request.getContentType())
                : ContentType.APPLICATION_OCTET_STREAM;

        req.setEntity(new InputStreamEntity(request.getInputStream(), ct));
        return req;
    }
}
