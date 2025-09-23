package org.example.metamapa.gestordatos.models.dtos.input;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CriterioRequest {
    private String tipo;
    private Map<String,String> params;
}
