package org.example.metamapa.agregador.models.repositorios;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IRepositorioHechos extends JpaRepository<Hecho, Long>{

}
