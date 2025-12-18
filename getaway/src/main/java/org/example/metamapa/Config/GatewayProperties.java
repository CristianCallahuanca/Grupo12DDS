package org.example.metamapa.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {
    private Map<String, String> servicios = new HashMap<>();
    public Map<String, String> getServicios() { return servicios; }
    public void setServicios(Map<String, String> servicios) { this.servicios = servicios; }
}

