package org.example.metamapa.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuenteMetamapaDTO {
    private Long id;
    private String nombreFuente;
    private String baseUrl;
}
