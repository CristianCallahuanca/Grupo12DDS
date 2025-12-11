package org.example.metamapa.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuenteMetamapaRequest {
    private String nombreFuente; // "MetaMapa Córdoba"
    private String baseUrl;      // "https://cordoba.metamapa.org/gestordatos"
}
