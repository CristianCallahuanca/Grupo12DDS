package org.example.metamapa.agregador.models.repositorios;

import org.example.metamapa.agregador.models.entidades.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IFuenteRepository extends JpaRepository<Fuente, String> {}

