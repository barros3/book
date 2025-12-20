package br.com.solucian.book.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(
        name = "AutorRequest",
        description = "Dados para criação ou atualização de um autor"
)
public record AutorRequest(

        @Schema(
                description = "Nome do autor",
                example = "Machado de Assis",
                maxLength = 40
        )
        @NotBlank(message = "O nome do autor é obrigatório")
        @Size(max = 40, message = "O nome do autor deve ter no máximo 40 caracteres")
        String nome
) {}