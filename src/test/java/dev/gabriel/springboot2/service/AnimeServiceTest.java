package dev.gabriel.springboot2.service;

import dev.gabriel.springboot2.controller.AnimeController;
import dev.gabriel.springboot2.domain.Anime;
import dev.gabriel.springboot2.exception.BadRequestException;
import dev.gabriel.springboot2.repository.AnimeRepository;
import dev.gabriel.springboot2.requests.AnimePostRequestBody;
import dev.gabriel.springboot2.requests.AnimePutRequestBody;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    // Quando quer testar a classe em si
    @InjectMocks
    private AnimeService animeService;

    // Utiliza para todas as classes que estão dentro da classe testada
    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createdValidAnime()));

        // BDDMockito Cria compotamentos para os métodos do  animeService
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeCreator.createdValidAnime()));

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createdValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createdValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createdValidAnime());

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("listAll returns list of anime inside page object when successful")
    void listAll_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createdValidAnime().getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllNonPageable returns list of anime when successful")
    void listAllNonPageable_ReturnsListOfAnime_WhenSuccessful() {
        String expectedName = AnimeCreator.createdValidAnime().getName();

        List<Anime> animes = animeService.listAllNonPageable();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns anime when successful")
    void findByIdOrThrowBadRequestException_ReturnsAnime_WhenSuccessful() {
        // Criar um comportamento Mockito para carregar um Anime quando acessar o método do service
        // Comparar um Anime carregado com o retorno do findByIdOrThrowBadRequestException mockito

        Long expectedId = AnimeCreator.createdValidAnime().getId();

        Anime anime = animeService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when anime not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimeNotFound() {
        // Criar um comportamento Mockito para carregar um Anime quando acessar o método do service
        // Comparar um Anime carregado com o retorno do findByIdOrThrowBadRequestException mockito

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(1));
    }

    @Test
    @DisplayName("findByName returns list of animes when successful")
    void findByName_ReturnsListOfAnimes_WhenSuccessful() {
        // Criar um comportamento Mockito para carregar a lista de animes quando acessar o método do service
        String expectedName = AnimeCreator.createdValidAnime().getName();

        // Aqui o valor passado não importa, pois o valor do findByName retornado está sendo criado a partir do mockito
        List<Anime> animes = animeService.findByName("anime");

        Assertions.assertThat(animes)
                .isNotNull();

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list when successful")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound() {
        // Quando você coloca esse compotamento dentro do método, ele tem preferência ao declarado no @BeforeEach
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animes = animeService.findByName("algum nome");

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns an anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {

        Anime animeSaved = animeService.save(AnimePostRequestBodyCreator.createdAnimeAnimePostRequestBody());

        Assertions.assertThat(animeSaved).isEqualTo(AnimeCreator.createdValidAnime());
    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful() {

        // Quando o retorno do método é void puro, só temos como testar a partir exceções
        Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.createdAnimeAnimePutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful() {
        // Quando o retorno do método é void puro, só temos como testar a partir exceções
        Assertions.assertThatCode(() -> animeService.delete(1))
                .doesNotThrowAnyException();
    }
}