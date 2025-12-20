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
        name = "AssuntoSimplesDTO",
        description = "Dados simplificados do assunto"
)
public class AssuntoSimplesDTO {

    @Schema(
            description = "Código do assunto",
            example = "1"
    )
    private Integer codAs;

    @Schema(
            description = "Descrição do assunto",
            example = "Romance Brasileiro"
    )
    private String descricao;
}
