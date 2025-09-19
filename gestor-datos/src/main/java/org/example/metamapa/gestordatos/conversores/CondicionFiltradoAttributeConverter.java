package org.example.metamapa.gestordatos.conversores;

import jakarta.persistence.AttributeConverter;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorCategoria;

public class CondicionFiltradoAttributeConverter implements AttributeConverter<CondicionDeFiltrado, String> {

    @Override
    public String convertToDatabaseColumn(CondicionDeFiltrado condicionFiltrado){

        if(condicionFiltrado == null){
            return null;
        }

        String condicion = "";

        switch (condicionFiltrado.getClass().getSimpleName()) {
            case "PorCategoria" -> condicion = "CATEGORIA";
            case "PorDescripcion" -> condicion = "DESCRIPCION";
            case "PorEstado" -> condicion = "ESTADO";
            case "PorEtiqueta" -> condicion = "ETIQUETA";
            case "PorFechaAcontecimiento" -> condicion = "FECHA_ACONTECIMIENTO";
            case "PorFechaCarga" -> condicion = "FECHA_CARGA";
            case "PorIdContribuyente" -> condicion = "ID_CONTRIBUYENTE";
            case "PorIDHecho" -> condicion = "ID_HECHO";
            case "PorOrigen" -> condicion = "ORIGEN";
            case "PorSinCategorizar" -> condicion = "SIN_CATEGORIZAR";
            case "PorTitulo" -> condicion = "TITULO";
            case "PorUbicacion" -> condicion = "UBICACION";
            default -> throw new IllegalArgumentException("Tipo desconocido: " + condicionFiltrado.getClass().getSimpleName());
        }

        return condicion;

    }

    @Override
    public CondicionDeFiltrado convertToEntityAttribute(String s){

        if(s == null){
            return null;
        }

        CondicionDeFiltrado condicionFiltrado;

        switch(s){
            case "PorCategoria" -> condicionFiltrado = new PorCategoria();

        }

    }

}
