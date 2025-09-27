package dinamico.models.repositorios;

import dinamico.models.entidades.hecho.HechoCrudo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepositorioHechosCrudos extends JpaRepository<HechoCrudo, Long> {

    List<HechoCrudo> findByFueLeidoFalse();
}

