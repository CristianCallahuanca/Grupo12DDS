package org.example.metamapa.estadisticas.Models.repositorios;

import org.example.metamapa.estadisticas.Models.entidades.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IColeccionesRepository extends JpaRepository<Coleccion, String> {

}
