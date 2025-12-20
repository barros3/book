package br.com.solucian.book.application.dto;

import br.com.solucian.book.domain.Assunto;
import br.com.solucian.book.domain.Autor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Schema(
        name = "LivroResponse",
        description = "Dados retornados do livro"
)
public record LivroResponse(

        @Schema(
                description = "Identificador do livro",
                example = "100"
        )
        Integer codl,

        @Schema(
                description = "Título do livro",
                example = "Dom Casmurro"
        )
        String titulo,

        @Schema(
                description = "Editora do livro",
                example = "Editora Globo"
        )
        String editora,

        @Schema(
                description = "Número da edição",
                example = "1"
        )
        Integer edicao,

        @Schema(
                description = "Ano de publicação",
                example = "1899"
        )
        String anoPublicacao,

        @Schema(
                description = "Valor do livro em moeda brasileira (R$)",
                example = "79.90"
        )
        BigDecimal valor,

        @Schema(
                description = "Autores do Livro",
                example = "Dostoiéviski"
        )
        Set<Autor> autores,

        @Schema(
                description = "Relatos de Voo sobre o triängulo das bermudas",
                example = "No caribe, um dos aviões da Frota Estelar"
        )
        Set<Assunto> assuntos
) {
}

