package cl.duoc.nutriexpert.registros.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

// Pregunta a ms-catalogo si un cultivo existe.
// Si ms-catalogo no responde, devuelve Optional.empty() y el registro se acepta igual:
// asi una caida del catalogo no bloquea la sincronizacion de la app.
@Component
public class CatalogoClient {

    private static final Logger log = LoggerFactory.getLogger(CatalogoClient.class);

    private final RestClient restClient;
    private final boolean validar;

    public CatalogoClient(@Value("${catalogo.url}") String url,
                          @Value("${catalogo.validar:true}") boolean validar) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3000);
        this.restClient = RestClient.builder().baseUrl(url).requestFactory(factory).build();
        this.validar = validar;
    }

    public Optional<Boolean> existeCultivo(String nombre) {
        if (!validar) {
            return Optional.of(true);
        }
        try {
            List<Map<String, Object>> cultivos = restClient.get()
                    .uri("/api/cultivos")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Map<String, Object>>>() { });
            if (cultivos == null) {
                return Optional.empty();
            }
            boolean existe = cultivos.stream()
                    .anyMatch(c -> nombre.equalsIgnoreCase(String.valueOf(c.get("nombre"))));
            return Optional.of(existe);
        } catch (Exception e) {
            log.warn("ms-catalogo no respondio; se acepta el cultivo '{}' sin validar: {}", nombre, e.getMessage());
            return Optional.empty();
        }
    }
}
