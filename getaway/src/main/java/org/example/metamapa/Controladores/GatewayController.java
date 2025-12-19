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


    @RequestMapping(value = "/graphql", method = {RequestMethod.POST, RequestMethod.GET})
    public void proxyGraphQL(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {

        String gestorBase = servicios.get("gestordatos");
        if (gestorBase == null) {
            response.sendError(500, "No está configurado gestordatos para GraphQL");
            return;
        }

        String targetUrl = gestorBase + "/graphql";

        log.info("Proxy GRAPHQL {} -> {}", request.getMethod(), targetUrl);

        HttpUriRequest proxyRequest;

        if ("GET".equals(request.getMethod())) {
            proxyRequest = new HttpGet(targetUrl +
                    (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
        } else {
            HttpPost post = new HttpPost(targetUrl);
            ContentType ct = request.getContentType() != null
                    ? ContentType.parse(request.getContentType())
                    : ContentType.APPLICATION_JSON;
            post.setEntity(new InputStreamEntity(request.getInputStream(), ct));
            proxyRequest = post;
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!headerName.equalsIgnoreCase("host")
                    && !headerName.equalsIgnoreCase("content-length")) {
                proxyRequest.addHeader(headerName, request.getHeader(headerName));
            }
        }

        try (CloseableHttpClient client = HttpClients.custom()
                .disableRedirectHandling()
                .build();
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




    @RequestMapping("/{modulo}/**")
    public void proxy(HttpServletRequest request,
                      HttpServletResponse response,
                      @PathVariable String modulo) throws IOException {

        String baseUrl = servicios.get(modulo);
        if (baseUrl == null) {
            response.sendError(400, "Módulo desconocido: " + modulo);
            return;
        }

        String fullPath = request.getRequestURI(); // ej: /gestordatos/contribuyentes/google

        if (fullPath.startsWith("/gestordatos/contribuyentes/google")) {
            log.info("OAUTH passthrough hit for {}", fullPath);

            String method = request.getMethod();
            if (!method.equals("GET") && !method.equals("HEAD")) {
                response.sendError(405, "Method not allowed");
                return;
            }

            String gestorBase = servicios.get("gestordatos");
            if (gestorBase == null) {
                response.sendError(500, "No está configurado gestordatos en gateway.servicios");
                return;
            }

            String target = gestorBase + fullPath
                    + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

            try (CloseableHttpClient client = HttpClients.custom()
                    .disableRedirectHandling() // 🔥 clave: NO seguir redirects
                    .build();
                 CloseableHttpResponse proxyResp = client.execute(new HttpGet(target))) {

                response.setStatus(proxyResp.getCode());

                // Copiar headers (Location + Set-Cookie son claves)
                for (Header h : proxyResp.getHeaders()) {
                    if (!h.getName().equalsIgnoreCase("transfer-encoding")) {
                        response.addHeader(h.getName(), h.getValue());
                    }
                }

                response.flushBuffer();
                return;
            }
        }

        // --- Proxy normal ---
        String path = request.getRequestURI().substring(("/" + modulo).length());

        String targetUrl = baseUrl + "/" + modulo + path
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        log.info("Proxy {} {} -> {}", request.getMethod(), request.getRequestURI(), targetUrl);

        HttpUriRequest proxyRequest;

        switch (request.getMethod()) {
            case "POST" -> proxyRequest = withBody(new HttpPost(targetUrl), request);
            case "PUT" -> proxyRequest = withBody(new HttpPut(targetUrl), request);
            case "PATCH" -> proxyRequest = withBody(new HttpPatch(targetUrl), request);
            case "DELETE" -> proxyRequest = new HttpDelete(targetUrl);
            case "HEAD" -> proxyRequest = new HttpHead(targetUrl);
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

        try (CloseableHttpClient client = HttpClients.custom()
                .disableRedirectHandling()
                .build();
             CloseableHttpResponse proxyResponse = client.execute(proxyRequest)) {

            response.setStatus(proxyResponse.getCode());

            for (Header header : proxyResponse.getHeaders()) {
                String name = header.getName();
                if (!name.equalsIgnoreCase("transfer-encoding")
                        && !name.equalsIgnoreCase("connection")
                        && !name.equalsIgnoreCase("keep-alive")
                        && !name.equalsIgnoreCase("proxy-authenticate")
                        && !name.equalsIgnoreCase("proxy-authorization")
                        && !name.equalsIgnoreCase("te")
                        && !name.equalsIgnoreCase("trailer")
                        && !name.equalsIgnoreCase("upgrade")) {
                    response.setHeader(name, header.getValue());
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
