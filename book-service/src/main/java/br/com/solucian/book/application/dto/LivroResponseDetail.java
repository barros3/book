package br.com.solucian.book.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "LivroResponseDetail",
        description = "Dados detalhados do livro"
)
public class LivroResponseDetail {

    @Schema(
            description = "Identificador do livro",
            example = "100"
    )
    private Integer codL;

    @Schema(
            description = "Título do livro",
            example = "Dom Casmurro"
    )
    private String titulo;

    @Schema(
            description = "Editora do livro",
            example = "Editora Globo"
    )
    private String editora;

    @Schema(
            description = "Número da edição",
            example = "1"
    )
    private Integer edicao;

    @Schema(
            description = "Ano de publicação",
            example = "1899"
    )
    private String anoPublicacao;

    @Schema(
            description = "Valor do livro em moeda brasileira (R$)",
            example = "79.90"
    )
    private BigDecimal valor;

    @Schema(
            description = "Lista de autores do livro"
    )
    private List<AutorSimplesDTO> autores;

    @Schema(
            description = "Lista de assuntos do livro"
    )
    private List<AssuntoSimplesDTO> assuntos;
}
