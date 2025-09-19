package org.example.metamapa.gestordatos.models.repositorios;

import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRepositorioHechos extends JpaRepository<Hecho, Long> {
}
