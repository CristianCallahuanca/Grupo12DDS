package org.example.metamapa.Servicios;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GeoIpService {

    private static final long CACHE_TTL_SECONDS = 1800; // 30 min

    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public String getCountryCode(String ip) {

        CacheEntry entry = cache.get(ip);

        if (entry != null && !entry.isExpired()) {
            return entry.countryCode;
        }

        try {
            String url = "https://ipapi.co/" + ip + "/country/";
            String countryCode = restTemplate.getForObject(url, String.class);

            if (countryCode == null || countryCode.isBlank()) {
                countryCode = "UNKNOWN";
            }

            cache.put(ip, new CacheEntry(countryCode.trim()));
            return countryCode.trim();

        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    /* ========================= */

    private static class CacheEntry {
        String countryCode;
        long timestamp;

        CacheEntry(String countryCode) {
            this.countryCode = countryCode;
            this.timestamp = Instant.now().getEpochSecond();
        }

        boolean isExpired() {
            return Instant.now().getEpochSecond() - timestamp > CACHE_TTL_SECONDS;
        }
    }
}