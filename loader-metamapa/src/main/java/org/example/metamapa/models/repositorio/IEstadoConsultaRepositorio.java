package org.example.metamapa.models.repositorio;


import org.example.metamapa.models.entidades.EstadoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEstadoConsultaRepositorio extends JpaRepository<EstadoConsulta, String> {
}
