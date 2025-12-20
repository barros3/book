package br.com.solucian.book.api;
import br.com.solucian.book.application.dto.AutorRequest;
import br.com.solucian.book.application.dto.AutorResponse;
import br.com.solucian.book.application.service.AutorService;
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
@RequestMapping("/api/autores")
@Tag(
        name = "Autores",
        description = "Operações de cadastro, consulta, atualização e remoção de autores"
)
public class AutorController {

    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Criar autor",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Autor criado",
                            content = @Content(schema = @Schema(implementation = AutorResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    public ResponseEntity<AutorResponse> criar(
            @RequestBody @Valid AutorRequest request
    ) {
        AutorResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    @Operation(
            summary = "Atualizar autor",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autor atualizado"),
                    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
            }
    )
    public ResponseEntity<AutorResponse> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid AutorRequest request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar autor por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autor encontrado"),
                    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
            }
    )
    public ResponseEntity<AutorResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar autores")
    public ResponseEntity<List<AutorResponse>> listar(@RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.listar(page, size));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Remover autor",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Autor removido"),
                    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
            }
    )
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
