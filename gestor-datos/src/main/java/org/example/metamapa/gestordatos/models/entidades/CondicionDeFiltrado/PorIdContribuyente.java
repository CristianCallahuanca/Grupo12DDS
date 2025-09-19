package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@DiscriminatorValue("POR_ID_CONTRIBUYENTE")
public class PorIdContribuyente extends CondicionDeFiltrado{

    @Column(name = "id_buscado")
    private String idBuscado;

    public PorIdContribuyente(String idBuscado) {
        this.idBuscado = idBuscado;
    }

    //TODO Lo tiene que ir a buscar a la BD cuando esté hecha
    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return true/* unHecho.getContribuyente_id().equals(idBuscado)*/;
    }
}
