package org.example.metamapa.models.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "estado_consulta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoConsulta {

    @Id
    private String loaderId;  // ej: "metamapa-cordoba"

    private LocalDateTime ultimaConsulta;

    private Integer cantidadHechos;

    private String estado; // "OK" o "ERROR"
}
