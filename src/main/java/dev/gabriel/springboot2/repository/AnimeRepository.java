package dev.gabriel.springboot2.repository;

import dev.gabriel.springboot2.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

}
