package br.com.solucian.book.application.dto;

import br.com.solucian.book.domain.Livro;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(
        name = "AutorResponse",
        description = "Dados retornados do autor"
)
public record AutorResponse(

        @Schema(
                description = "Identificador do autor",
                example = "1"
        )
        Integer codAu,

        @Schema(
                description = "Nome do autor",
                example = "Machado de Assis"
        )
        String nome,

        @Schema(
                description = "Lista de Livros",
                example = "O Poder da Ação, A Arte da Guerra, A lógica da Vida"
        )
        Set<Livro> livros
) {}