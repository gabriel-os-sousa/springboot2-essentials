package dev.gabriel.springboot2.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
// Quando tenho o build, preciso de um @AllArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequestBody {
    // @NotNull faz parte do @NotEmpty
    // @NotNull(message = "The anime name cannot be null") // Atributo não pode ser nulo
    @NotEmpty(message = "The anime name cannot be empty") // Atributo não pode ser vazio
    private String name;
}
