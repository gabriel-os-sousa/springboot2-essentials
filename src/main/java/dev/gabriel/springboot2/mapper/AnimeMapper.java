package dev.gabriel.springboot2.mapper;

import dev.gabriel.springboot2.domain.Anime;
import dev.gabriel.springboot2.requests.AnimePostRequestBody;
import dev.gabriel.springboot2.requests.AnimePutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
    // Pega a instancia dessa classe
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    /* Converte o AnimePostRequestBody para Anime*/
    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);

    /* Converte o AnimePutRequestBody para Anime*/
    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);
}
