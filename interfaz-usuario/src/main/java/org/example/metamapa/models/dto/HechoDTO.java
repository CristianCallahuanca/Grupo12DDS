package org.example.metamapa.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HechoDTO {

    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "Debes seleccionar o ingresar una categoría")
    private String categoria;

    // Este campo lo llenará el usuario si elige "Otra" en el desplegable
    private String categoriaOtra;

    @NotBlank(message = "La latitud es obligatoria")
    private String latitud;

    @NotBlank(message = "La longitud es obligatoria")
    private String longitud;

    @NotBlank(message = "La fecha del acontecimiento es obligatoria")
    private String fechaAcontecimiento;

    @NotBlank(message = "Debes ingresar al menos una etiqueta")
    private String etiqueta;

    // No es obligatorio, así que no lleva anotación de validación
    private List<String> archivosMultimedia;

    // Este campo lo llenaremos en el backend, no viene del formulario
    private String contribuyenteID;

    private String origen;

}