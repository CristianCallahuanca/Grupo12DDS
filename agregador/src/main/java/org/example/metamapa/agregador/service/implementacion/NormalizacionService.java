package org.example.metamapa.agregador.service.implementacion;

import org.example.metamapa.agregador.models.dtos.HechoDTO;

import java.util.List;

public class NormalizacionService {

    private List<String> catalogoCategorias = List.of(
            "vientos fuertes",
            "inundaciones",
            "granizo",
            "nevadas",
            "calor extremo",
            "sequía",
            "derrumbes",
            "actividad volcánica",
            "incendios",
            "contaminación",
            "evento sanitario",
            "derrame",
            "intoxicación masiva"
    );

    private void normalizarUbicacion(HechoDTO hechoSinNormalizar){
        String latitudNormalizada = hechoSinNormalizar.getLatitud().replace(",", ".");
        hechoSinNormalizar.setLatitud(latitudNormalizada);
    }

    public void normalizarUbicaciones(List<HechoDTO> hechosCrudos){
        hechosCrudos.forEach(this::normalizarUbicacion);
    }

    private String categoriaEnCatalogo(String categoria){
        return catalogoCategorias.stream()
                .filter(cat -> categoria.toLowerCase()
                        .contains(cat.toLowerCase()))
                .findFirst().orElse("No cotemplado");
    }

    private void normalizarCategoria(HechoDTO hechoSinNormalizar){
        String newCategory = categoriaEnCatalogo(hechoSinNormalizar.getCategoria());
        hechoSinNormalizar.setCategoria(newCategory);
    }

    //TO DO los no contemplado deberian ser seteados por un admin
    public void normalizarCategorias(List<HechoDTO> hechoSinNormalizar){
        hechoSinNormalizar.forEach(this::normalizarCategoria);
    }



}




















