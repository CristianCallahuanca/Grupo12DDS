package org.example.metamapa.agregador.models.repositorios;

import org.example.metamapa.agregador.models.entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {

}
