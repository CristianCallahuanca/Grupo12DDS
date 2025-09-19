package org.example.metamapa.agregador.models.repositorios;

import org.example.metamapa.agregador.models.entidades.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISpamRepository  extends JpaRepository<SolicitudEliminacion, Long> { }
