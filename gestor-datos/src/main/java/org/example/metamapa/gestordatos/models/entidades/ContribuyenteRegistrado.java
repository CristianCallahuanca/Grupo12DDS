package org.example.metamapa.gestordatos.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contribuyente_registrado")
public class ContribuyenteRegistrado  {

        @Id
        private String id;

        @Column(name = "nombre")
        private String nombre;

        @Column(name = "apellido")
        private String apellido;

        @Column(name = "dni")
        private Integer dni;

        @Column(name = "edad")
        private Integer edad;

        @OneToMany(mappedBy = "contribuyente")
        private List<Hecho> hechos;

        public ContribuyenteRegistrado(String nombre, String apellido, int edad, int dni) {
                this.nombre = nombre;
                this.apellido = apellido;
                this.edad = edad;
                this.dni = dni;
        }

}
