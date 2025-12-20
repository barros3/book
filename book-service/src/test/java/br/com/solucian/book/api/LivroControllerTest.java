package br.com.solucian.book.api;

import br.com.solucian.book.api.exceptions.GlobalExceptionHandler;
import br.com.solucian.book.application.dto.*;
import br.com.solucian.book.application.service.AssuntoService;
import br.com.solucian.book.application.service.AutorService;
import br.com.solucian.book.application.service.LivroService;
import br.com.solucian.book.domain.Assunto;
import br.com.solucian.book.domain.Autor;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LivroController.class)
@Import(GlobalExceptionHandler.class)
class LivroControllerTest {

    private static final Logger log = LoggerFactory.getLogger(LivroControllerTest.class);

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LivroService service;

    @MockBean
    AutorService autorService;

    @MockBean
    AssuntoService assuntoService;

    private static final String BASE = "/api/livros";

    @BeforeEach
    void logTest(TestInfo info) {
        log.info(">>> Executando: {}", info.getDisplayName());
    }

    @Test
    @DisplayName("POST /api/livros -> 201")
    void deveriaRetornar_201() throws Exception {
        Autor autor = new Autor();
        autor.setCodAu(1);
        autor.setNome("Autor Teste");

        Assunto assunto = new Assunto();
        assunto.setCodAs(1);
        assunto.setDescricao("Assunto Teste");

        var response = new LivroResponse(
                1,
                "Livro A",
                "Editora X",
                1,
                "2024",
                new BigDecimal("79.90"),
                Set.of(autor),
                Set.of(assunto)
        );

        Mockito.when(service.criar(any(LivroRequest.class))).thenReturn(response);

        var body = """
            {
              "titulo":"Livro A",
              "editora":"Editora X",
              "edicao":1,
              "anoPublicacao":"2024",
              "valor":79.90,
              "autoresIds":[1,2],
              "assuntosIds":[3]
            }
        """;

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codl").value(1))
                .andExpect(jsonPath("$.valor").value(79.90));
    }

    @Test
    @DisplayName("POST /api/livros -> 400 (validação)")
    void deveriaRetornar_400() throws Exception {
        var body = """
            {
              "titulo":"",
              "editora":"Editora X",
              "edicao":0,
              "anoPublicacao":"20",
              "valor":-1,
              "autoresIds":[],
              "assuntosIds":[]
            }
        """;

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("POST /api/livros -> 422 (autor/assunto inexistente ou regra do banco)")
    void deveriaRetornar_409() throws Exception {
        Mockito.when(service.criar(any(LivroRequest.class)))
                .thenThrow(new DataIntegrityViolationException("FK constraint violation"));

        var body = """
            {
              "titulo":"Livro A",
              "editora":"Editora X",
              "edicao":1,
              "anoPublicacao":"2024",
              "valor":79.90,
              "autoresIds":[999],
              "assuntosIds":[999]
            }
        """;

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("GET /api/livros/{id} -> 200")
    void buscar_200() throws Exception {
        AutorSimplesDTO autorDTO = AutorSimplesDTO.builder()
                .codAu(1)
                .nome("Autor Teste")
                .build();

        AssuntoSimplesDTO assuntoDTO = AssuntoSimplesDTO.builder()
                .codAs(1)
                .descricao("Assunto Teste")
                .build();

        LivroResponseDetail responseDetail = LivroResponseDetail.builder()
                .codL(10)
                .titulo("Livro X")
                .editora("Editora Y")
                .edicao(2)
                .anoPublicacao("2020")
                .valor(new BigDecimal("50.00"))
                .autores(List.of(autorDTO))
                .assuntos(List.of(assuntoDTO))
                .build();

        Mockito.when(service.buscarPorId(10)).thenReturn(responseDetail);

        mockMvc.perform(get(BASE + "/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codL").value(10))
                .andExpect(jsonPath("$.titulo").value("Livro X"))
                .andExpect(jsonPath("$.editora").value("Editora Y"));
    }

    @Test
    @DisplayName("GET /api/livros/{id} -> 404")
    void buscar_404() throws Exception {
        Mockito.when(service.buscarPorId(999))
                .thenThrow(new EntityNotFoundException("Livro não encontrado"));

        mockMvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /api/livros -> 200 (listar)")
    void listar_200() throws Exception {
        Autor autor1 = new Autor();
        autor1.setCodAu(1);
        autor1.setNome("Autor 1");

        Autor autor2 = new Autor();
        autor2.setCodAu(2);
        autor2.setNome("Autor 2");

        Assunto assunto1 = new Assunto();
        assunto1.setCodAs(1);
        assunto1.setDescricao("Assunto 1");

        Assunto assunto2 = new Assunto();
        assunto2.setCodAs(2);
        assunto2.setDescricao("Assunto 2");

        Mockito.when(service.listar(anyInt(), anyInt())).thenReturn(List.of(
                new LivroResponse(1, "A", "E1", 1, "2020", new BigDecimal("10.00"),
                        Set.of(autor1), Set.of(assunto1)),
                new LivroResponse(2, "B", "E2", 1, "2021", new BigDecimal("20.00"),
                        Set.of(autor2), Set.of(assunto2))
        ));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].codl").value(1))
                .andExpect(jsonPath("$[1].codl").value(2));
    }

    @Test
    @DisplayName("PATCH /api/livros/{id} -> 200")
    void atualizar_200() throws Exception {
        Autor autor = new Autor();
        autor.setCodAu(1);
        autor.setNome("Autor Atualizado");

        Assunto assunto1 = new Assunto();
        assunto1.setCodAs(1);
        assunto1.setDescricao("Assunto 1");

        Assunto assunto2 = new Assunto();
        assunto2.setCodAs(2);
        assunto2.setDescricao("Assunto 2");

        Mockito.when(service.atualizar(eq(3), any(LivroCreateRequest.class)))
                .thenReturn(new LivroResponse(3, "Atual", "Editora", 1, "2024",
                        new BigDecimal("99.90"), Set.of(autor), Set.of(assunto1, assunto2)));

        var body = """
            {
              "titulo":"Atual",
              "editora":"Editora",
              "edicao":1,
              "anoPublicacao":"2024",
              "valor":99.90,
              "autoresIds":[1],
              "assuntosIds":[2,1]
            }
        """;

        mockMvc.perform(patch(BASE + "/{id}", 3)
                        .contentType("application/json-patch+json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codl").value(3))
                .andExpect(jsonPath("$.titulo").value("Atual"))
                .andExpect(jsonPath("$.valor").value(99.90));
    }

    @Test
    @DisplayName("DELETE /api/livros/{id} -> 204")
    void remover_204() throws Exception {
        Mockito.doNothing().when(service).remover(5);

        mockMvc.perform(delete(BASE + "/{id}", 5))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/livros/{id} -> 404")
    void remover_404() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Livro não encontrado"))
                .when(service).remover(999);

        mockMvc.perform(delete(BASE + "/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("POST /api/livros -> 500 (erro inesperado)")
    void deveriaRetornar_500() throws Exception {
        Mockito.when(service.criar(any(LivroRequest.class)))
                .thenThrow(new RuntimeException("Erro interno do servidor"));

        var body = """
            {
              "titulo":"Livro A",
              "editora":"Editora X",
              "edicao":1,
              "anoPublicacao":"2024",
              "valor":79.90,
              "autoresIds":[1],
              "assuntosIds":[1]
            }
        """;

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500));
    }

}
