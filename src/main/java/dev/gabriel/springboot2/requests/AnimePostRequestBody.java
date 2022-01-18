package dev.gabriel.springboot2.requests;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class AnimePostRequestBody {
    // @NotNull faz parte do @NotEmpty
    // @NotNull(message = "The anime name cannot be null") // Atributo não pode ser nulo
    @NotEmpty(message = "The anime name cannot be empty") // Atributo não pode ser vazio
    private String name;
}
