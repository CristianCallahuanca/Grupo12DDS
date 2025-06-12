package SolicitudEliminar;



import java.util.ArrayList;
import java.util.List;

public class DetectorDeSpamSingleton implements DetectorDeSpam {
    private static final DetectorDeSpamSingleton instancia = new DetectorDeSpamSingleton();

    private DetectorDeSpamSingleton() {}


    public static DetectorDeSpamSingleton getInstance() {
        return instancia;
    }

    @Override
    public boolean esSpam(String texto) {
        return texto.toLowerCase().contains("dinero") || texto.contains("$$$");
    }


}
