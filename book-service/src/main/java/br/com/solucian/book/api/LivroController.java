package br.com.solucian.book.api;

import br.com.solucian.book.application.dto.LivroCreateRequest;
import br.com.solucian.book.application.dto.LivroRequest;
import br.com.solucian.book.application.dto.LivroResponse;
import br.com.solucian.book.application.dto.LivroResponseDetail;
import br.com.solucian.book.application.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
@Tag(
        name = "Livros",
        description = "Operações de cadastro, consulta, atualização e remoção de livros"
)
public class LivroController {

    private final LivroService service;

    public LivroController(LivroService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Criar livro",
            description = "Cria um livro com autores, assuntos e valor monetário",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Livro criado",
                            content = @Content(schema = @Schema(implementation = LivroResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "422", description = "Autor ou assunto inexistente")
            }
    )
    public ResponseEntity<LivroResponse> criar(
            @RequestBody @Valid LivroRequest request
    ) {
        LivroResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(path = "/{id}" , consumes = "application/json-patch+json")
    @Operation(
            summary = "Atualizar livro",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Livro atualizado"),
                    @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
                    @ApiResponse(responseCode = "422", description = "Autor ou assunto inexistente")
            }
    )
    public ResponseEntity<LivroResponse> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid LivroCreateRequest request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar livro por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Livro encontrado"),
                    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
            }
    )
    public ResponseEntity<LivroResponseDetail> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar livros")
    public ResponseEntity<List<LivroResponse>> listar(@RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.listar(page, size));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Remover livro",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Livro removido"),
                    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
            }
    )
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

}

