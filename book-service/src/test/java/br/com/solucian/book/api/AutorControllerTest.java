package br.com.solucian.book.api;

import br.com.solucian.book.api.exceptions.GlobalExceptionHandler;
import br.com.solucian.book.application.dto.AutorRequest;
import br.com.solucian.book.application.dto.AutorResponse;
import br.com.solucian.book.application.service.AutorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AutorController.class)
@Import(GlobalExceptionHandler.class)
class AutorControllerTest {

    private static final Logger log = LoggerFactory.getLogger(AutorControllerTest.class);

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean AutorService service;

    private static final String BASE = "/api/autores";

    @BeforeEach
    void logTest(TestInfo info) {
        log.info(">>> Executando: {}", info.getDisplayName());
    }

    @Test
    @DisplayName("POST /api/autores -> 201 (criar autor)")
    void deveriaRetornar_201() throws Exception {
        var request = new AutorRequest("Machado de Assis");
        var response = new AutorResponse(1, "Machado de Assis", Set.of());

        Mockito.when(service.criar(any(AutorRequest.class))).thenReturn(response);

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codAu").value(1))
                .andExpect(jsonPath("$.nome").value("Machado de Assis"));
    }

    @Test
    @DisplayName("POST /api/autores -> 400 (validação)")
    void deveriaRetornar_400() throws Exception {
        var body = """
            {"nome":"   "}
        """;

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Dados inválidos"));
    }

    @Test
    @DisplayName("POST /api/autores -> 409 (regra negócio / objeto já existe no banco)")
    void deveriaRetornar_409() throws Exception {
        Mockito.when(service.criar(any(AutorRequest.class)))
                .thenThrow(new DataIntegrityViolationException("violação unique"));

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Autor Duplicado"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("POST /api/autores -> 500 (erro inesperado)")
    void deveriaRetornar_500() throws Exception {
        Mockito.when(service.criar(any(AutorRequest.class)))
                .thenThrow(new RuntimeException("boom"));

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Qualquer"}
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    @DisplayName("GET /api/autores/{id} -> 200 (buscar por id)")
    void buscarPorId_200() throws Exception {
        Mockito.when(service.buscarPorId(10))
                .thenReturn(new AutorResponse(10, "Clarice Lispector", Set.of()));

        mockMvc.perform(get(BASE + "/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codAu").value(10))
                .andExpect(jsonPath("$.nome").value("Clarice Lispector"));
    }

    @Test
    @DisplayName("GET /api/autores/{id} -> 404 (não encontrado)")
    void buscarPorId_404() throws Exception {
        Mockito.when(service.buscarPorId(999))
                .thenThrow(new EntityNotFoundException("Autor não encontrado"));

        mockMvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("O autor solicitado não foi encontrado em nosso sistema"));
    }

    @Test
    @DisplayName("GET /api/autores -> 200 (listar)")
    void listar_200() throws Exception {
        Mockito.when(service.listar(anyInt(), anyInt())).thenReturn(List.of(
                new AutorResponse(1, "Autor A", Set.of()),
                new AutorResponse(2, "Autor B", Set.of())
        ));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("PATCH /api/autores/{id} -> 200 (atualizar)")
    void atualizar_200() throws Exception {
        Mockito.when(service.atualizar(eq(3), any(AutorRequest.class)))
                .thenReturn(new AutorResponse(3, "Atualizado", Set.of()));

        mockMvc.perform(patch(BASE + "/{id}", 3)
                        .contentType("application/json-patch+json")
                        .content("""
                                {"nome":"Atualizado"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codAu").value(3));
    }

    @Test
    @DisplayName("PATCH /api/autores/{id} -> 400 (validação)")
    void atualizar_400() throws Exception {
        mockMvc.perform(patch(BASE + "/{id}", 3)
                        .contentType("application/json-patch+json")
                        .content("""
                                {"nome":""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("DELETE /api/autores/{id} -> 204 (remover)")
    void remover_204() throws Exception {
        Mockito.doNothing().when(service).remover(5);

        mockMvc.perform(delete(BASE + "/{id}", 5))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/autores/{id} -> 404 (não encontrado)")
    void remover_404() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Autor não encontrado"))
                .when(service).remover(999);

        mockMvc.perform(delete(BASE + "/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}