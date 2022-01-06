package dev.gabriel.springboot2.repository;

import dev.gabriel.springboot2.domain.Anime;

import java.util.List;

public interface AnimeRepository {
    List<Anime> listAll();
}
