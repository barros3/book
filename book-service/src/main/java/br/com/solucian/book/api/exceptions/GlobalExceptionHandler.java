package br.com.solucian.book.api.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String CLIENTE_NOME = "BookStore API";

    /* ==============================
       400 – Erros de validação (DTO)
       ============================== */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationError(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> {
                    String fieldName = formatFieldName(e.getField());
                    String message = e.getDefaultMessage();

                    if (message != null) {
                        if (message.contains("não pode estar vazio") || message.contains("não pode ser nulo")) {
                            return String.format("O campo '%s' é obrigatório", fieldName);
                        }
                        if (message.contains("deve ser um endereço de e-mail bem formado")) {
                            return String.format("O '%s' informado não é um e-mail válido", fieldName);
                        }
                        if (message.contains("tamanho deve estar entre")) {
                            return String.format("O campo '%s' tem tamanho inválido", fieldName);
                        }
                    }

                    return String.format("Campo '%s': %s", fieldName, message);
                })
                .toList();

        String mensagemPrincipal = errors.size() == 1
                ? "Dados inválidos"
                : String.format("Foram encontrados %d erros de validação", errors.size());

        return ResponseEntity.badRequest().body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Erro de Validação",
                        mensagemPrincipal,
                        errors,
                        request.getRequestURI(),
                        CLIENTE_NOME
                )
        );
    }

    /* ==============================
       400 – Constraint violations
       ============================== */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> {
                    String field = formatFieldName(v.getPropertyPath().toString());
                    return String.format("Restrição no campo '%s': %s", field, v.getMessage());
                })
                .toList();

        return ResponseEntity.badRequest().body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Dados Inválidos",
                        "Os dados enviados violam regras de validação",
                        errors,
                        request.getRequestURI(),
                        CLIENTE_NOME
                )
        );
    }

    /* ==============================
       400 – JSON inválido
       ============================== */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidJson(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        String causa = ex.getMostSpecificCause().getMessage();
        String mensagem;

        if (causa.contains("java.util.Date") || causa.contains("java.time")) {
            mensagem = "Formato de data/hora inválido. Use o formato: yyyy-MM-dd";
        } else if (causa.contains("NumberFormatException")) {
            mensagem = "Valor numérico inválido. Verifique se os números estão no formato correto";
        } else if (causa.contains("Enum")) {
            mensagem = "Valor inválido para uma opção de seleção";
        } else {
            mensagem = "O formato do JSON está incorreto ou contém valores inválidos";
        }

        return ResponseEntity.badRequest().body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "JSON Inválido",
                        mensagem,
                        List.of("Detalhe técnico: " + causa),
                        request.getRequestURI(),
                        CLIENTE_NOME
                )
        );
    }

    /* ==============================
       404 – Entidade não encontrada
       ============================== */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            EntityNotFoundException ex,
            HttpServletRequest request
    ) {
        String mensagem = ex.getMessage();
        String mensagemCliente;

        if (mensagem == null || mensagem.isEmpty()) {
            mensagemCliente = "O recurso solicitado não foi encontrado";
        } else if (mensagem.contains("Autor") || mensagem.contains("autor")) {
            mensagemCliente = "O autor solicitado não foi encontrado em nosso sistema";
        } else if (mensagem.contains("Livro") || mensagem.contains("livro")) {
            mensagemCliente = "O livro solicitado não foi encontrado em nosso catálogo";
        } else if (mensagem.contains("Cliente") || mensagem.contains("cliente")) {
            mensagemCliente = "O cliente informado não foi encontrado";
        } else if (mensagem.contains("Venda") || mensagem.contains("venda")) {
            mensagemCliente = "A venda solicitada não foi encontrada";
        } else {
            mensagemCliente = "O registro solicitado não foi encontrado";
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        "Não Encontrado",
                        mensagemCliente,
                        List.of(mensagem),
                        request.getRequestURI(),
                        CLIENTE_NOME
                )
        );
    }

    /* ==============================
       409 – Conflito (duplicação)
       ============================== */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        String mensagem = ex.getMessage();
        String mensagemCliente;

        if (mensagem != null) {
            if (mensagem.contains("Duplicate entry") || mensagem.contains("duplicate key")) {
                mensagemCliente = "Já existe um registro com essas informações no sistema";
            } else if (mensagem.contains("foreign key constraint") || mensagem.contains("REFERENCES")) {
                mensagemCliente = "Não é possível realizar esta operação devido a referências existentes";
            } else if (mensagem.contains("cannot be null") || mensagem.contains("NOT NULL")) {
                mensagemCliente = "Algumas informações obrigatórias não foram fornecidas";
            } else {
                mensagemCliente = "Os dados enviados conflitam com as regras do sistema";
            }
        } else {
            mensagemCliente = "Erro de integridade de dados";
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        "Conflito de Dados",
                        mensagemCliente,
                        List.of(mensagem != null ? mensagem : "Violação de integridade"),
                        request.getRequestURI(),
                        CLIENTE_NOME
                )
        );
    }

    /* ==============================
       422 – Erros de regra de negócio
       ============================== */
    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<ApiErrorResponse> handleBadSqlGrammar(
            BadSqlGrammarException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        "Erro na Consulta",
                        "Ocorreu um erro ao processar sua consulta",
                        List.of("Detalhe técnico: " + ex.getSQLException().getMessage()),
                        request.getRequestURI(),
                        CLIENTE_NOME
                )
        );
    }

    /* ==============================
       500 – Erro de Relatório
       ============================== */
    @ExceptionHandler({RelatorioException.class, JRException.class})
    public ResponseEntity<ApiErrorResponse> handleRelatorioException(
            Exception ex,
            HttpServletRequest request
    ) {
        ex.printStackTrace();

        String mensagemCliente;
        if (ex instanceof RelatorioException) {
            mensagemCliente = "Não foi possível gerar o relatório solicitado";
        } else {
            mensagemCliente = "Erro técnico ao processar o relatório";
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Erro no Relatório",
                        mensagemCliente,
                        List.of("Detalhe: " + ex.getMessage()),
                        request.getRequestURI(),
                        CLIENTE_NOME
                )
        );
    }

    /* ==============================
       500 – Erro inesperado
       ============================== */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericError(
            Exception ex,
            HttpServletRequest request
    ) {
        ex.printStackTrace();

        String mensagemCliente = "Ocorreu um erro inesperado em nosso sistema. " +
                "Nossa equipe já foi notificada e está trabalhando para resolver o problema.";

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Erro Interno",
                        mensagemCliente,
                        List.of("Detalhe técnico: " + ex.getClass().getSimpleName() + " - " + ex.getMessage()),
                        request.getRequestURI(),
                        CLIENTE_NOME
                )
        );
    }

    /* ==============================
       Métodos auxiliares
       ============================== */

    /**
     * Formata o nome do campo para algo mais legível
     */
    private String formatFieldName(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return "campo";
        }

        fieldName = fieldName.replaceAll("^(this\\.)?(.+)", "$2");

        String formatted = fieldName.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );

        return formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
    }
}