package org.example.metamapa.agregador.service.implementacion;

import jakarta.transaction.Transactional;
import org.example.metamapa.agregador.models.dtos.DTO_IN.HechoDTO_IN;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.example.metamapa.agregador.service.IAgregacionService;
import org.example.metamapa.agregador.service.IDuplicacionService;
import org.example.metamapa.agregador.service.INormalizacionService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgregacionService implements IAgregacionService{
    private final RestClient estatico, dinamico, proxy;

    private final INormalizacionService normalizacionService;
    private final IDuplicacionService duplicacionService;
    private final IRepositorioHechos hechosRepository;

    public AgregacionService(INormalizacionService normalizacionService, IDuplicacionService duplicacionService,RestClient loaderEstatico,
                             RestClient loaderDinamico, RestClient loaderProxy,IRepositorioHechos hechosRepository) {
        this.estatico = loaderEstatico;
        this.dinamico = loaderDinamico;
        this.proxy = loaderProxy;
        this.normalizacionService = normalizacionService;
        this.duplicacionService = duplicacionService;
        this.hechosRepository = hechosRepository;
    }

    public List<HechoDTO_IN> getHechosDTO3FuentesSinLimpiar() {
        List<HechoDTO_IN> all = new ArrayList<>();
        //all.addAll(listar(estatico));
        all.addAll(listar(dinamico));
        //all.addAll(listar(proxy));
        System.out.println("se recibieron de las fuentes: " + all.size() + "hechos");

        return all;
    }


    private List<HechoDTO_IN> listar(RestClient c) {
        return c.get().uri("/hechos")
                .retrieve().body(new ParameterizedTypeReference<List<HechoDTO_IN>>() {});
    }

    //Atte GPT:
    public static String obtenerProvinciaAPI(double lat, double lon) {
        String urlString = String.format(
                "https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json",
                lat, lon
        );

        try {
            // Hacer la request
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "JavaApp"); // Nominatim requiere un User-Agent

            // Leer la respuesta
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parsear el JSON con Jackson
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.toString());

            // Navegar hasta "address" -> "state"
            JsonNode addressNode = root.path("address");
            return addressNode.path("state").asText();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Transactional
    public void integrarHechosFuentes(){

        //obtenermos todos los hechos de las fuentes los normalizamos sacamos duplicados y los almacenamos en BD

         List<Hecho> hechos = normalizacionService.normalizarHechos(this.getHechosDTO3FuentesSinLimpiar());
         //List<Hecho> hechos_finales = duplicacionService.eliminarHechosRepetidos(hechos);

         System.out.println("la provincia obtenida de la api es: " + obtenerProvinciaAPI(hechos.get(0).getUbicacion().getLatitud(),hechos.get(0).getUbicacion().getLongitud()));

         hechosRepository.saveAll(hechos);
    }
}