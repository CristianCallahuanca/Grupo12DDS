package org.example.metamapa.Controladores;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GatewayController {

    private final Map<String, String> servicios = new HashMap<>();

    public GatewayController() {
        servicios.put("gestordatos", "http://localhost:8500");
        servicios.put("fuenteDinamica", "http://localhost:8102");
        servicios.put("agregador", "http://localhost:8200");
        servicios.put("estadisticas", "http://localhost:8600");
        servicios.put("fuenteDemo", "http://localhost:8700");
        servicios.put("fuenteEstatica", "http://localhost:8101");
        servicios.put("fuenteMetamapa", "http://localhost:8103");
    }

    @RequestMapping("/{modulo}/**")
    public void proxy(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable String modulo
    ) throws IOException {

        String baseUrl = servicios.get(modulo);
        if (baseUrl == null) {
            response.sendError(400, "Módulo desconocido");
            return;
        }

        String path = request.getRequestURI().substring(("/" + modulo).length());


        String targetUrl = baseUrl + "/" + modulo + path +
                (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        System.out.println("Path: " + targetUrl);

        HttpUriRequest proxyRequest;

        switch (request.getMethod()) {
            case "POST" -> {
                HttpPost post = new HttpPost(targetUrl);
                post.setEntity(
                        new InputStreamEntity(
                                request.getInputStream(),
                                ContentType.parse(request.getContentType())
                        )
                );
                proxyRequest = post;
            }
            case "PUT" -> {
                HttpPut put = new HttpPut(targetUrl);
                put.setEntity(
                        new InputStreamEntity(
                                request.getInputStream(),
                                ContentType.parse(request.getContentType())
                        )
                );
                proxyRequest = put;
            }
            default -> proxyRequest = new HttpGet(targetUrl);
        }

        // Copiar headers (menos los peligrosos)
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!headerName.equalsIgnoreCase("host")
                    && !headerName.equalsIgnoreCase("content-length")) {
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
                proxyResponse.getEntity()
                        .getContent()
                        .transferTo(response.getOutputStream());
            }
        }
    }
}
