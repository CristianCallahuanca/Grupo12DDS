package org.example.metamapa.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class HechoDTO {
    private String id;
    private String fuente;   // "estatico", "dinamico", "proxy"
    private String titulo;
}
