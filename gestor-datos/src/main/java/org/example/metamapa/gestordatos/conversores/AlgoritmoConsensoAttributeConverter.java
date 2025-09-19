package org.example.metamapa.gestordatos.conversores;

import jakarta.persistence.AttributeConverter;

import jakarta.persistence.Converter;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorCategoria;
import org.example.metamapa.gestordatos.models.entidades.Consenso.Absoluto;
import org.example.metamapa.gestordatos.models.entidades.Consenso.AlgoritmoConsenso;
import org.example.metamapa.gestordatos.models.entidades.Consenso.MayoriaSimple;
import org.example.metamapa.gestordatos.models.entidades.Consenso.MultiplesMenciones;

@Converter(autoApply = true)
public class AlgoritmoConsensoAttributeConverter implements AttributeConverter<AlgoritmoConsenso, String> {

    @Override
    public String convertToDatabaseColumn(AlgoritmoConsenso algoritmo){

        if(algoritmo == null){
            return null;
        }

        String condicion = "";

        switch (algoritmo.getClass().getSimpleName()) {
            case "Absoluto" -> condicion = "ABSOLUTO";
            case "MayoriaSimple" -> condicion = "MAYORIA_SIMPLE";
            case "MultiplesMenciones" -> condicion = "MULTIPLES_MENCIONES";
            default -> throw new IllegalArgumentException("Tipo desconocido: " + algoritmo.getClass().getSimpleName());
        }

        return condicion;

    }

    @Override
    public AlgoritmoConsenso convertToEntityAttribute(String s){

        if(s == null){
            return null;
        }

        AlgoritmoConsenso algoritmo = null;

        switch(s){
            case "ABSOLUTO" -> {
                algoritmo = new Absoluto();
                break;
            }
            case "MAYORIA_SIMPLE" -> {
                algoritmo = new MayoriaSimple();
                break;
            }
            case "MULTIPLES_MENCIONES" -> {
                algoritmo = new MultiplesMenciones();
                break;
            }
            default -> throw new IllegalArgumentException("Tipo desconocido" + s);
        }

        return algoritmo;

    }
}
