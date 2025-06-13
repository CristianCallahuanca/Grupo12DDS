package HechosSinRevisar;

import AdministracionDeHechos.Hecho;

import java.util.ArrayList;
import java.util.List;

public class HechosSinRevisar {

    private static final HechosSinRevisar instancia = new HechosSinRevisar();
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

    public boolean contiene(Hecho hecho) {
        return hechos.contains(hecho);
    }

}
