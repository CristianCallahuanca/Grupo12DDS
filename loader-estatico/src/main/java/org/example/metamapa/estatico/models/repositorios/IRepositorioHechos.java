package org.example.metamapa.estatico.models.repositorios;

import org.example.metamapa.estatico.models.entidades.HechoCrudo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepositorioHechos extends JpaRepository<HechoCrudo, Long> {
    List<HechoCrudo> findByEnviadoFalse();
}
