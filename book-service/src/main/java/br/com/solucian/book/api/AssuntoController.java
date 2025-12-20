package br.com.solucian.book.api;

import br.com.solucian.book.application.dto.AssuntoRequest;
import br.com.solucian.book.application.dto.AssuntoResponse;
import br.com.solucian.book.application.dto.DataResponseDTO;
import br.com.solucian.book.application.service.AssuntoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.hibernate.query.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assuntos")
@Tag(
        name = "Assuntos",
        description = "Operações de cadastro, consulta, atualização e remoção de assuntos"
)
public class AssuntoController {

    private final AssuntoService service;

    public AssuntoController(AssuntoService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Criar assunto",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Assunto criado",
                            content = @Content(schema = @Schema(implementation = AssuntoResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    public ResponseEntity<AssuntoResponse> criar(
            @RequestBody @Valid AssuntoRequest request
    ) {
        AssuntoResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    @Operation(
            summary = "Atualizar assunto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Assunto atualizado"),
                    @ApiResponse(responseCode = "404", description = "Assunto não encontrado")
            }
    )
    public ResponseEntity<AssuntoResponse> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid AssuntoRequest request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar assunto por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Assunto encontrado"),
                    @ApiResponse(responseCode = "404", description = "Assunto não encontrado")
            }
    )
    public ResponseEntity<AssuntoResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar assuntos")
    public ResponseEntity<List<AssuntoResponse>> listar(@RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.listar(page, size));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Remover assunto",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Assunto removido"),
                    @ApiResponse(responseCode = "404", description = "Assunto não encontrado")
            }
    )
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
