package dev.gabriel.springboot2.client;

import dev.gabriel.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 6);
        log.info(entity);

        Anime obj = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 6);
        log.info(obj);

        Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class, 6);
        log.info(Arrays.toString(animes));

        // Aqui eu não restorno o Array, mas sim um List
        // O exchange é mais se for passar algumas coisas na requisição, Ex: headers de autenticação e etc
        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8080/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        log.info(exchange.getBody());
    }
}
