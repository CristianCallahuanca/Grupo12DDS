package AdministracionDeHechos;

import java.util.ArrayList;
import java.util.List;

public class HechosSinRevisar {

    private static HechosSinRevisar instancia = new HechosSinRevisar();
    private List<Hecho> hechos = new ArrayList<>();

    private HechosSinRevisar() {}

    public static HechosSinRevisar getInstance() {
        return instancia;
    }

    public void agregarHecho(Hecho hecho) {
        hechos.add(hecho);
    }

    public void sacarHecho(Hecho hecho) {
        hechos.remove(hecho);
    }

    public List<Hecho> obtenerHechosSinRevisar() {
        return hechos;
    }
}

