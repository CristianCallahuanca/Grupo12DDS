package org.example.metamapa.estatico.models.entidades;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElementoCSV {
    private String archivoCSV;
    private Integer ultimaFilaLeida;
    private Boolean procesado;

    public ElementoCSV(String archivoCSV, Integer ultimaFilaLeida) {
        this.archivoCSV = archivoCSV;
        this.ultimaFilaLeida = ultimaFilaLeida;
        this.procesado = false;
    }

    public void actualizarUltimaFilaLeida(){
        this.ultimaFilaLeida = this.ultimaFilaLeida + 1;
    }
}
