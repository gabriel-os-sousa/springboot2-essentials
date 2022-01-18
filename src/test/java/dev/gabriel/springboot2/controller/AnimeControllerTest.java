package dev.gabriel.springboot2.controller;

import dev.gabriel.springboot2.domain.Anime;
import dev.gabriel.springboot2.requests.AnimePostRequestBody;
import dev.gabriel.springboot2.requests.AnimePutRequestBody;
import dev.gabriel.springboot2.service.AnimeService;
import dev.gabriel.springboot2.util.AnimeCreator;
import dev.gabriel.springboot2.util.AnimePostRequestBodyCreator;
import dev.gabriel.springboot2.util.AnimePutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    // Quando quer testar a classe em si
    @InjectMocks
    private AnimeController animeController;

    // Utiliza para todas as classes que estão dentro da classe testada
    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createdValidAnime()));

        // BDDMockito Cria compotamentos para os métodos do  animeService
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.listAllNonPageable())
                .thenReturn(List.of(AnimeCreator.createdValidAnime()));

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createdValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createdValidAnime()));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createdValidAnime());

        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        BDDMockito.doNothing().when(animeServiceMock).deleteById(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createdValidAnime().getName();

        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns list of anime inside page object when successful")
    void listAll_ReturnsListOfAnime_WhenSuccessful() {
        String expectedName = AnimeCreator.createdValidAnime().getName();

        List<Anime> animes = animeController.listAll().getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful() {
        // Criar um comportamento Mockito para carregar um Anime quando acessar o método do service
        // Comparar um Anime carregado com o retorno do findByIdOrThrowBadRequestException mockito

        Long expectedId = AnimeCreator.createdValidAnime().getId();

        Anime anime = animeController.findById(1).getBody();

        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of animes when successful")
    void findByName_ReturnsListOfAnimes_WhenSuccessful() {
        // Criar um comportamento Mockito para carregar a lista de animes quando acessar o método do service
        String expectedName = AnimeCreator.createdValidAnime().getName();

        // Aqui o valor passado não importa, pois o valor do findByName retornado está sendo criado a partir do mockito
        List<Anime> animes = animeController.findByName("anime").getBody();

        Assertions.assertThat(animes)
                .isNotNull();

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list when successful")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound() {
        // Quando você coloca esse compotamento dentro do método, ele tem preferência ao declarado no @BeforeEach
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animes = animeController.findByName("algum nome").getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns an anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {

        Anime animeSaved = animeController.save(AnimePostRequestBodyCreator.createdAnimeAnimePostRequestBody()).getBody();

        Assertions.assertThat(animeSaved).isEqualTo(AnimeCreator.createdValidAnime());
    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful() {

        // Quando o retorno do método é void, temos algumas maneiras de verificar o retorno
        Assertions.assertThatCode(() -> animeController.replace(AnimePutRequestBodyCreator.createdAnimeAnimePutRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.createdAnimeAnimePutRequestBody());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful() {

        // Quando o retorno do método é void, temos algumas maneiras de verificar o retorno
        Assertions.assertThatCode(() -> animeController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.delete(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}