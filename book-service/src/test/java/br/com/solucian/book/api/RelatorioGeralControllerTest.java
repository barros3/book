package br.com.solucian.book.api;

import br.com.solucian.book.api.exceptions.GlobalExceptionHandler;
import br.com.solucian.book.application.dto.VendaAutorDto;
import br.com.solucian.book.application.service.RelatorioGeralService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RelatorioGeralController.class)
@Import(GlobalExceptionHandler.class)
class RelatorioGeralControllerTest {

    private static final Logger log = LoggerFactory.getLogger(RelatorioGeralControllerTest.class);

    @Autowired MockMvc mockMvc;

    @MockBean RelatorioGeralService service;

    private static final String BASE = "/api/relatorio-geral";

    @BeforeEach
    void logTest(TestInfo info) {
        log.info(">>> Executando: {}", info.getDisplayName());
    }

    @Test
    @DisplayName("GET /api/relatorio-geral-autor -> 200 (sem filtros)")
    void consultar_200_semFiltros() throws Exception {

        Mockito.when(service.consultar())
                .thenReturn((List<VendaAutorDto>) List.of(
                        new VendaAutorDto(
                                "10",
                                "Dom Casmurro",
                                "Assunto 1, Assunto 2",
                                "Livro 1, Livro 2",
                                1,
                                2,
                                new BigDecimal("79.90"),
                                new Date()


                        )
                ));

        mockMvc.perform(get("/api/relatorio-geral"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].codigo").value("10"))
                .andExpect(jsonPath("$[0].autor").value("Dom Casmurro"))
                .andExpect(jsonPath("$[0].vendaTotal").value(79.90));
    }

    @Test
    @DisplayName("GET /api/relatorio-geral -> 500 (erro inesperado)")
    void consultar_500() throws Exception {
        Mockito.when(service.consultar())
                .thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get(BASE))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500));
    }
}