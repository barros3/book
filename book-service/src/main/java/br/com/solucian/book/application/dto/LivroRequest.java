package br.com.solucian.book.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Schema(
        name = "LivroRequest",
        description = "Dados para criação de um livro"
)
public record LivroRequest(

        @NotBlank(message = "Título é obrigatório")
        @Size(max = 40, message = "Título deve ter no máximo 40 caracteres")
        @Schema(
                description = "Título do livro",
                example = "Dom Casmurro",
                required = true
        )
        String titulo,

        @NotBlank(message = "Editora é obrigatória")
        @Size(max = 40, message = "Editora deve ter no máximo 40 caracteres")
        @Schema(
                description = "Editora do livro",
                example = "Editora Globo",
                required = true
        )
        String editora,

        @NotNull(message = "Edição é obrigatória")
        @Min(value = 1, message = "Edição deve ser maior que zero")
        @Schema(
                description = "Número da edição",
                example = "1",
                required = true
        )
        Integer edicao,

        @NotBlank(message = "Ano de publicação é obrigatório")
        @Size(min = 4, max = 4, message = "Ano de publicação deve ter 4 dígitos")
        @Pattern(regexp = "\\d{4}", message = "Ano de publicação deve conter apenas números")
        @Schema(
                description = "Ano de publicação",
                example = "2024",
                required = true
        )
        String anoPublicacao,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        @Schema(
                description = "Valor do livro em moeda brasileira (R$)",
                example = "79.90",
                required = true
        )
        BigDecimal valor,

        @NotEmpty(message = "Deve ter pelo menos um autor")
        @Schema(
                description = "IDs dos autores do livro",
                example = "[1, 2]",
                required = true
        )
        List<Integer> autoresIds,

        @NotEmpty(message = "Deve ter pelo menos um assunto")
        @Schema(
                description = "IDs dos assuntos do livro",
                example = "[3, 4]",
                required = true
        )
        List<Integer> assuntosIds
) {
}
