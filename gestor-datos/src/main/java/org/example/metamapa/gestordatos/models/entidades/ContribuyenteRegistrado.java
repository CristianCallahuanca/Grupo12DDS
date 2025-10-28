package org.example.metamapa.gestordatos.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contribuyente_registrado")
public class ContribuyenteRegistrado  {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long userId;

        @Column(name = "nombre")
        private String nombre;

        @Column(name = "apellido")
        private String apellido;

        @Column(name = "dni")
        private Integer dni;

        @Column(name = "fecha_nacimiento")
        private Date fecha_nacimiento;

        @Column(name = "email")
        private String email;

        @Column(name = "password")
        private String password;

        @OneToMany(mappedBy = "contribuyente")
        private List<Hecho> hechos;

        @Enumerated(EnumType.STRING) // Guarda el nombre del enum en la BD
        private Rol rol = Rol.USER; // Valor por defecto

        public ContribuyenteRegistrado(String nombre, String apellido, Date fecha_nacimiento, int dni, String email, String password) {
                this.nombre = nombre;
                this.apellido = apellido;
                this.fecha_nacimiento = fecha_nacimiento;
                this.dni = dni;
                this.email = email;
                this.password = password;
        }

}
