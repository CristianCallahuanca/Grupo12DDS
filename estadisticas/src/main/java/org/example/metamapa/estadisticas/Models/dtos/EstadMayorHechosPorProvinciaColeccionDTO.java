package org.example.metamapa.estadisticas.Models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

// 1) De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos reportados?
@Data
@AllArgsConstructor
public class EstadMayorHechosPorProvinciaColeccionDTO {
    LocalDateTime fechaCalculo;
    String coleccionTitulo;
    String provincia;
    Integer cantidadHechos;
}