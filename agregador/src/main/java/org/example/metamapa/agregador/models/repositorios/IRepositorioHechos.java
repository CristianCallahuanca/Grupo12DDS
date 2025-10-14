package org.example.metamapa.agregador.models.repositorios;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepositorioHechos extends JpaRepository<Hecho, Long>{
    List<Hecho> findByUbicacion_ProvinciaIsNullOrUbicacion_Provincia(String provincia);
}
