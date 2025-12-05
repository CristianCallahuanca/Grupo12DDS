package org.example.metamapa.gestordatos.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.metamapa.gestordatos.models.entidades.enums.Provider;

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

        @Column(name = "email")
        private String email;

        @Column(name = "password")
        private String password;

        @OneToMany(mappedBy = "contribuyente")
        private List<Hecho> hechos;

        @Enumerated(EnumType.STRING) // Guarda el nombre del enum en la BD
        private Rol rol = Rol.USER; // Valor por defecto

        //SSO

        @Column(name = "google_id", unique = true)
        private String googleId;  // El ID único que Google da (campo "sub")

        @Column(name = "provider", nullable = false)
        @Enumerated(EnumType.STRING)
        private Provider provider;  //LOCAL, GOOGLE, HYBRID

        public ContribuyenteRegistrado(String nombre, String apellido, String email, String password,String googleId ,Provider provider) {
                this.nombre = nombre;
                this.apellido = apellido;
                this.email = email;
                this.password = password;
                this.googleId = googleId;
                this.provider = provider;
        }

}
