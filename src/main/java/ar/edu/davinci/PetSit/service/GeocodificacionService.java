package ar.edu.davinci.PetSit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Convierte una dirección de texto a coordenadas lat/lng
 * usando la API gratuita de Nominatim (OpenStreetMap).
 *
 * Sin API key. Límite: 1 request/segundo (más que suficiente
 * para geocodificar al guardar una vet o refugio).
 *
 * Docs: https://nominatim.org/release-docs/develop/api/Search/
 */
@Service
public class GeocodificacionService {

    private final Logger LOGGER = LoggerFactory.getLogger(GeocodificacionService.class);

    private static final String NOMINATIM_URL =
        "https://nominatim.openstreetmap.org/search?format=json&limit=1&q=";

    // User-Agent obligatorio para Nominatim (política de uso)
    private static final String USER_AGENT = "PetSit/1.0 (tesis davinci)";

    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Resultado de geocodificación.
     * Si no encontró nada, lat y lng son null.
     */
    public record Coordenadas(Double lat, Double lng) {
        public boolean encontrado() { return lat != null && lng != null; }
    }

    /**
     * Geocodifica la dirección dada.
     *
     * @param direccion  texto libre: "Moldes 2345, Belgrano, Buenos Aires"
     * @return Coordenadas con lat/lng, o Coordenadas(null, null) si falló
     */
    public Coordenadas geocodificar(String direccion) {
        if (direccion == null || direccion.isBlank()) {
            return new Coordenadas(null, null);
        }

        try {
            // Agregar "Argentina" si no está ya para mejorar precisión
            String query = direccion;
            if (!direccion.toLowerCase().contains("argentina") &&
                !direccion.toLowerCase().contains("buenos aires")) {
                query = direccion + ", Buenos Aires, Argentina";
            }

            String url = NOMINATIM_URL + URLEncoder.encode(query, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", USER_AGENT)
                .header("Accept-Language", "es")
                .timeout(Duration.ofSeconds(8))
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                LOGGER.warn("Nominatim respondió {}: {}", response.statusCode(), query);
                return new Coordenadas(null, null);
            }

            JsonNode root = objectMapper.readTree(response.body());

            if (root.isArray() && root.size() > 0) {
                JsonNode first = root.get(0);
                double lat = first.get("lat").asDouble();
                double lng = first.get("lon").asDouble();
                LOGGER.info("Geocodificado '{}' → ({}, {})", direccion, lat, lng);
                return new Coordenadas(lat, lng);
            }

            LOGGER.warn("Nominatim no encontró resultados para: {}", query);
            return new Coordenadas(null, null);

        } catch (Exception e) {
            LOGGER.error("Error geocodificando '{}': {}", direccion, e.getMessage());
            return new Coordenadas(null, null);
        }
    }
}
