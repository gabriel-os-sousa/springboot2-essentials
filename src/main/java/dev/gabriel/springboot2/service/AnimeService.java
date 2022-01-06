package dev.gabriel.springboot2.service;

import dev.gabriel.springboot2.domain.Anime;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeService {
    public List<Anime> listAll() {
        return List.of(new Anime(1L,"Naruto"), new Anime(2L,"DBZ"));
    }
}
