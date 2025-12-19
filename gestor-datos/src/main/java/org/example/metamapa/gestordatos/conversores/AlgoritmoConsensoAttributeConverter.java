package org.example.metamapa.gestordatos.conversores;

import jakarta.persistence.AttributeConverter;

import jakarta.persistence.Converter;
import org.example.metamapa.gestordatos.models.Consenso.Absoluto;
import org.example.metamapa.gestordatos.models.Consenso.AlgoritmoConsenso;
import org.example.metamapa.gestordatos.models.Consenso.MayoriaSimple;
import org.example.metamapa.gestordatos.models.Consenso.MultiplesMenciones;

@Converter(autoApply = true)
public class AlgoritmoConsensoAttributeConverter implements AttributeConverter<AlgoritmoConsenso, String> {

    @Override
    public String convertToDatabaseColumn(AlgoritmoConsenso algoritmo) {
        if (algoritmo == null) return null;

        return switch (algoritmo.getClass().getSimpleName()) {
            case "Absoluto" -> "ABSOLUTO";
            case "MayoriaSimple" -> "MAYORIA_SIMPLE";
            case "MultiplesMenciones" -> "MULTIPLES_MENCIONES";
            case "SinAlgoritmo" -> "SIN_ALGORITMO";
            default -> throw new IllegalArgumentException(
                    "Tipo de algoritmo desconocido: " + algoritmo.getClass().getSimpleName());
        };
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
            default -> throw new IllegalArgumentException("Tipo de algoritmo desconocido: " + s);
        }

        return algoritmo;

    }
}
