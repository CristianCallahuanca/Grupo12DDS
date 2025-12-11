package org.example.metamapa.loaderdemo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuenteDemoRequest {
    private String nombreFuente; // ej: "DDSI-Desastres-Naturales"
    private String url;          // ej: "https://api-ddsi.disilab.ar/public"
}
