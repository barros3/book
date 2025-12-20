package br.com.solucian.book.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(
        name = "AssuntoRequest",
        description = "Dados para criação ou atualização de um assunto"
)
public record AssuntoRequest(

        @Schema(
                description = "Descrição do assunto",
                example = "Literatura",
                maxLength = 20
        )
        @NotBlank(message = "A descrição do assunto é obrigatória")
        @Size(max = 20, message = "A descrição deve ter no máximo 20 caracteres")
        String descricao
) {}
