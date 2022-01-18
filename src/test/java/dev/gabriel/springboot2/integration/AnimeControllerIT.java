package dev.gabriel.springboot2.integration;

import dev.gabriel.springboot2.domain.Anime;
import dev.gabriel.springboot2.repository.AnimeRepository;
import dev.gabriel.springboot2.requests.AnimePostRequestBody;
import dev.gabriel.springboot2.util.AnimeCreator;
import dev.gabriel.springboot2.util.AnimePostRequestBodyCreator;
import dev.gabriel.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

// 1 - Iniciar todo o spring
// 2 - Passa uma porta aleatoria toda vez que os testes forem executados
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

// Test com banco de dados em mempória
@AutoConfigureTestDatabase
// Da um drop no banco antes de cada método
@DirtiesContext(classMode =  DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        // Como o banco sobe com a aplicação, toda vez estará vazio
        // Então vamos adicionar um Anime toda vez que for executar este método
        Anime savedAnime = animeRepository.save(AnimeCreator.createdAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns list of anime inside page object when successful")
    void listAll_ReturnsListOfAnime_WhenSuccessful() {
        // Como o banco sobe com a aplicação, toda vez estará vazio
        // Então vamos adicionar um Anime toda vez que for executar este método
        Anime savedAnime = animeRepository.save(AnimeCreator.createdAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful() {
        // Como o banco sobe com a aplicação, toda vez estará vazio
        // Então vamos adicionar um Anime toda vez que for executar este método
        Anime savedAnime = animeRepository.save(AnimeCreator.createdAnimeToBeSaved());

        Long expectedId = savedAnime.getId();

        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);

//        Anime anime = testRestTemplate.exchange("/animes/id", HttpMethod.GET, null,
//                new ParameterizedTypeReference<Anime>() {
//                }).getBody();
        Assertions.assertThat(anime.getId()).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of animes when successful")
    void findByName_ReturnsListOfAnimes_WhenSuccessful() {
        // Como o banco sobe com a aplicação, toda vez estará vazio
        // Então vamos adicionar um Anime toda vez que for executar este método
        List<Anime> savedAnimes = List.of(animeRepository.save(AnimeCreator.createdAnimeToBeSaved()));

        String expectedName = savedAnimes.get(0).getName();

        String url = String.format("/animes/find?name=%s", expectedName);

        List<Anime> animes = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull();

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list when successful")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound() {
        // Não precisamos de criar um Anime ou uma lista de Animes, pois queremos testar o caso de não achar nada (Emptylist)
        List<Anime> animes = testRestTemplate.exchange("/animes/find?name=semnome", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns an anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createdAnimeAnimePostRequestBody();
        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes", animePostRequestBody, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful() {
        // Só posso alterar alguma coisa se já estiver no banco de dados, por isso cria um anime antes de tudo
        Anime savedAnime = animeRepository.save(AnimeCreator.createdAnimeToBeSaved());

        savedAnime.setName("Alterado");

        // testRestTemplate.put retorn void e não o Wrapper Void.class, com isso impossibilita verificar null
        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/",
                HttpMethod.PUT,
                new HttpEntity<>(savedAnime),
                Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful() {
        // Só posso deletar alguma coisa se já estiver no banco de dados, por isso cria um anime antes de tudo
        Anime savedAnime = animeRepository.save(AnimeCreator.createdAnimeToBeSaved());

        // testRestTemplate.put retorn void e não o Wrapper Void.class, com isso impossibilita verificar null
        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
