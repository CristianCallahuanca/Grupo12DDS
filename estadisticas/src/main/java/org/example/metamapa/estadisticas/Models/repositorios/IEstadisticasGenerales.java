package org.example.metamapa.estadisticas.Models.repositorios;


import org.example.metamapa.estadisticas.Models.entidades.EstadisticaGeneral;
import org.example.metamapa.estadisticas.Models.entidades.HechosPorProvincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IEstadisticasGenerales extends JpaRepository<EstadisticaGeneral, Long> {

}
