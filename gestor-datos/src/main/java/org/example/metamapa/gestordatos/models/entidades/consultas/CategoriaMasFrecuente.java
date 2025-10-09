package org.example.metamapa.gestordatos.models.entidades.consultas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Categoria_mas_frecuente")
public class CategoriaMasFrecuente {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "cantidad")
    private long cantidad;

    public CategoriaMasFrecuente(String categoria, long cantidad) {
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.id = UUID.randomUUID().toString();
    }
}