package dev.gabriel.springboot2.util;

import dev.gabriel.springboot2.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {
    public static AnimePostRequestBody createdAnimeAnimePostRequestBody () {
        return AnimePostRequestBody.builder()
                .name(AnimeCreator.createdAnimeToBeSaved().getName())
                .build();
    }
}
