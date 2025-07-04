package AdministracionDeHechos;
import Fuentes.Fuente;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import Fuentes.FuenteDinamica;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import Servicios.ServicioFiltradorDeHechos;
import lombok.Getter;
import lombok.Setter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Coleccion {
    private List<Fuente> fuentes;
    private String titulo;
    private String descripcion;
    private List<CriterioDePertenencia> criterios;
    private List<Hecho> hechos;
    private String handle;

    //Si List<CriterioDePertenencia> criterios es null puede romper
    public Coleccion(List<Fuente> fuentes, String titulo, String descripcion, List<CriterioDePertenencia> criterios) throws IOException {
        this.fuentes = fuentes;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterios = criterios;
        this.handle = generarHandle(titulo);
        this.cargarHechos(fuentes,criterios);
        this.cargarColeccion();
    }


    private String generarHandle(String titulo) {
        // Quita acentos
        String normalizado = Normalizer.normalize(titulo, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        // Elimina caracteres no alfanuméricos excepto espacios, y reemplaza espacios por guiones
        return normalizado.toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", "-");
    }



    private void cargarColeccion() {
        ColeccionRepositoryEnMemoria.getInstancia().guardar(this);
    }

    public void cargarHechos(List<Fuente> fuentes, List<CriterioDePertenencia> criterios) throws IOException {
        fuentes.forEach(unaFuente -> {
            try {
                this.cargarHechosDeUnaFuente(unaFuente, criterios);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void cargarHechosDeUnaFuente(Fuente fuente, List<CriterioDePertenencia> criterios) throws IOException {
        this.hechos.addAll(ServicioFiltradorDeHechos.filtrarHechos(fuente.obtenerHechos(),criterios));

    }

    public List<Hecho> obtenerHechos() {
        return hechos.stream().filter(hecho->hecho.getVisible()).toList();
    }

    //Solo para tests
    public void imprimirHechos(List<Hecho> unosHechos) {
        unosHechos.forEach(unHecho -> unHecho.imprimirHecho());
    }



}

