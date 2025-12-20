package br.com.solucian.book.api;

import br.com.solucian.book.api.exceptions.GlobalExceptionHandler;
import br.com.solucian.book.application.dto.AssuntoResponse;
import br.com.solucian.book.application.service.AssuntoService;
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

@WebMvcTest(controllers = AssuntoController.class)
@Import(GlobalExceptionHandler.class)
class AssuntoControllerTest {

    private static final Logger log = LoggerFactory.getLogger(AssuntoControllerTest.class);

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean AssuntoService service;

    private static final String BASE = "/api/assuntos";

    @BeforeEach
    void logTest(TestInfo info) {
        log.info(">>> Executando: {}", info.getDisplayName());
    }

    @Test
    @DisplayName("POST /api/assuntos -> 201 (criar)")
    void deveriaRetornar_201() throws Exception {
        Mockito.when(service.criar(any()))
                .thenReturn(new AssuntoResponse(1, "Romance", Set.of()));

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"descricao":"Romance"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codAs").value(1));
    }

    @Test
    @DisplayName("POST /api/assuntos -> 400 (validação)")
    void deveriaRetornar_400() throws Exception {
        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"descricao":"  "}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("POST /api/assuntos -> 409 (regra negócio / objeto já existe no banco)")
    void deveriaRetornar_409() throws Exception {
        Mockito.when(service.criar(any()))
                .thenThrow(new DataIntegrityViolationException("violação"));

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"descricao":"Duplicado"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("GET /api/assuntos/{id} -> 200")
    void buscar_200() throws Exception {
        Mockito.when(service.buscarPorId(10)).thenReturn(new AssuntoResponse(10, "Ação", Set.of()));

        mockMvc.perform(get(BASE + "/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codAs").value(10));
    }

    @Test
    @DisplayName("GET /api/assuntos/{id} -> 404")
    void buscar_404() throws Exception {
        Mockito.when(service.buscarPorId(999))
                .thenThrow(new EntityNotFoundException("Assunto não encontrado"));

        mockMvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /api/assuntos -> 200 (listar)")
    void listar_200() throws Exception {
        Mockito.when(service.listar(anyInt(), anyInt())).thenReturn(List.of(
                new AssuntoResponse(1, "A", Set.of()),
                new AssuntoResponse(2, "B", Set.of())
        ));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("PATCH /api/assuntos/{id} -> 200")
    void atualizar_200() throws Exception {
        Mockito.when(service.atualizar(eq(3), any()))
                .thenReturn(new AssuntoResponse(3, "Atualizado", Set.of()));

        mockMvc.perform(patch(BASE + "/{id}", 3)
                        .contentType("application/json-patch+json")
                        .content("""
                                {"descricao":"Atualizado"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codAs").value(3));
    }

    @Test
    @DisplayName("DELETE /api/assuntos/{id} -> 204")
    void remover_204() throws Exception {
        Mockito.doNothing().when(service).remover(5);

        mockMvc.perform(delete(BASE + "/{id}", 5))
                .andExpect(status().isNoContent());
    }
}