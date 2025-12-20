package br.com.solucian.book.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "AutorSimplesDTO",
        description = "Dados simplificados do autor"
)
public class AutorSimplesDTO {

    @Schema(
            description = "Código do autor",
            example = "1"
    )
    private Integer codAu;

    @Schema(
            description = "Nome do autor",
            example = "Machado de Assis"
    )
    private String nome;
}
