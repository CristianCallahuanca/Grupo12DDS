package org.example.metamapa.estadisticas.Servicios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository                                          //TODO: Cambiar tipo según que se guarde
public interface IEstadisticaService extends JpaRepository<String, String> {
}
