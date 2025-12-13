package org.example.metamapa.Servicios;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

@Service
public class GeoIpService {

    private final DatabaseReader databaseReader;

    public GeoIpService() throws IOException {
        InputStream dbStream =
                getClass().getResourceAsStream("/geoip/GeoLite2-Country.mmdb");

        if (dbStream == null) {
            throw new IllegalStateException("GeoLite2 database not found");
        }

        this.databaseReader = new DatabaseReader.Builder(dbStream).build();
    }

    public String getCountryCode(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            CountryResponse response = databaseReader.country(address);
            return response.getCountry().getIsoCode(); // ej: "AR"
        } catch (Exception e) {
            return null;
        }
    }
}
