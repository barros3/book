package br.com.solucian.book.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "RelatorioGeralResponse", description = "Linha do relatório geral consolidado")
public record RelatorioGeralResponse(

        @Schema(description = "Código do livro", example = "10")
        Integer codl,

        @Schema(description = "Título do livro", example = "Dom Casmurro")
        String titulo,

        @Schema(description = "Autor", example = "Machado de Assis")
        String autor,

        @Schema(description = "Assunto", example = "Literatura Brasileira")
        String assunto,

        @Schema(description = "Editora", example = "Editora Exemplo")
        String editora,

        @Schema(description = "Edição", example = "1")
        Integer edicao,

        @Schema(description = "Ano de publicação (YYYY)", example = "1899")
        String anoPublicacao,

        @Schema(description = "Valor do livro", example = "79.90")
        BigDecimal valor,

        @Schema(description = "Atualizado em", example = "2025-12-15T23:10:00")
        LocalDateTime atualizadoEm
) {}
