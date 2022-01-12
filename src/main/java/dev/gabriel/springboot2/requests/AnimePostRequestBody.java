package dev.gabriel.springboot2.requests;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

@Data
public class AnimePostRequestBody {
    @NotEmpty(message = "The anime name cannot be empty") // Atributo não pode ser vazio
    // @NotNull faz parte do @NotEmpty
    // @NotNull(message = "The anime name cannot be null") // Atributo não pode ser nulo
    private String name;
}
