package org.example.metamapa.gestordatos.models.repositorios.consultas;

import org.example.metamapa.gestordatos.models.entidades.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICantSolicitudesSpam extends JpaRepository<Coleccion, String> {
}
