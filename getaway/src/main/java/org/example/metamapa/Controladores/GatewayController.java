package org.example.metamapa.Controladores;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.HashMap;
import java.util.Map;

@RestController
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

        // Parte extra después de /{modulo}
        String fullUri = request.getRequestURI();           // ej: /gestordatos/admin/origenes-reales
        String rest = fullUri.substring(("/" + modulo).length()); // ej: /admin/origenes-reales
        if (rest.isEmpty()) rest = "/";

        String targetUrl = baseUrl + rest
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        HttpUriRequest proxyRequest;

        switch (request.getMethod()) {
            case "POST" -> {
                HttpPost post = new HttpPost(targetUrl);
                post.setEntity(new InputStreamEntity(
                        request.getInputStream(),
                        request.getContentType() != null
                                ? ContentType.parse(request.getContentType())
                                : ContentType.APPLICATION_OCTET_STREAM
                ));
                proxyRequest = post;
            }
            case "PUT" -> {
                HttpPut put = new HttpPut(targetUrl);
                put.setEntity(new InputStreamEntity(
                        request.getInputStream(),
                        request.getContentType() != null
                                ? ContentType.parse(request.getContentType())
                                : ContentType.APPLICATION_OCTET_STREAM
                ));
                proxyRequest = put;
            }
            case "PATCH" -> {
                HttpPatch patch = new HttpPatch(targetUrl);
                patch.setEntity(new InputStreamEntity(
                        request.getInputStream(),
                        request.getContentType() != null
                                ? ContentType.parse(request.getContentType())
                                : ContentType.APPLICATION_OCTET_STREAM
                ));
                proxyRequest = patch;
            }
            case "DELETE" -> proxyRequest = new HttpDelete(targetUrl);
            case "GET" -> proxyRequest = new HttpGet(targetUrl);
            default -> {
                response.sendError(405, "Método no soportado: " + request.getMethod());
                return;
            }
        }

        // Copiar headers (menos host/content-length)
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
}

