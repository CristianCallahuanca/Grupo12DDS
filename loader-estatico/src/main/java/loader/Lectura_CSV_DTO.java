package loader;

import lombok.Getter;
import lombok.Setter;
import org.example.metamapa.estatico.models.entidades.ElementoCSV;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;

import java.util.List;

@Getter
@Setter
public class Lectura_CSV_DTO {
    private ElementoCSV elementoCSV;
    private List<HechoCrudo> hechoCrudos;

    public Lectura_CSV_DTO(ElementoCSV elementoCSV, List<HechoCrudo> hechoCrudos){
        this.elementoCSV = elementoCSV;
        this.hechoCrudos = hechoCrudos;
    }
}
