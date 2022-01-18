package dev.gabriel.springboot2.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
// O @Build precisa de um @AllArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class AnimePutRequestBody {
    private Long id;
    private String name;
}
