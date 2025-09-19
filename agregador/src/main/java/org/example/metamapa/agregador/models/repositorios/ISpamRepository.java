package org.example.metamapa.agregador.models.repositorios;

import org.example.metamapa.agregador.models.entidades.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISpamRepository  extends JpaRepository<SolicitudEliminacion, Long> { }
