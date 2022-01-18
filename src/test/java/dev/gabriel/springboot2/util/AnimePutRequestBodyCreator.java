package dev.gabriel.springboot2.util;

import dev.gabriel.springboot2.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {
    public static AnimePutRequestBody createdAnimeAnimePutRequestBody () {
        return AnimePutRequestBody.builder()
                .id(AnimeCreator.createdValidUpdatedAnime().getId())
                .name(AnimeCreator.createdValidUpdatedAnime().getName())
                .build();
    }
}
