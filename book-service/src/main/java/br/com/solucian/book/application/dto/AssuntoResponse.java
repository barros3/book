package br.com.solucian.book.application.dto;

import br.com.solucian.book.domain.Livro;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(
        name = "AssuntoResponse",
        description = "Dados retornados do assunto"
)
public record AssuntoResponse (

        @Schema(
                description = "Identificador do assunto",
                example = "10"
        )
        Integer codAs,

        @Schema(
                description = "Descrição do assunto",
                example = "Literatura"
        )
        String descricao,
        @Schema(
                description = "Livros",
                example = "Crime e Castigo"
        )
        Set<Livro> livros
) {}