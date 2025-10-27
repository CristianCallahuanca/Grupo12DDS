package org.example.metamapa.gestordatos.models.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriterioRequest {
    private String tipo;
    private Map<String,String> params;

    public CriterioRequest(String tipo, String valor) {
        this.tipo = tipo;
        this.params = Map.of("valor", valor);
    }

}
