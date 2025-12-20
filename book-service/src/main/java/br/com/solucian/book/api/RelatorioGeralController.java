package br.com.solucian.book.api;

import br.com.solucian.book.application.dto.VendaAutorDto;
import br.com.solucian.book.application.service.RelatorioGeralService;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/relatorio-geral")
@Tag(
        name = "Relatório Geral",
        description = "Consulta consolidada de livros, autores e assuntos"
)
public class RelatorioGeralController {

    private final RelatorioGeralService service;

    public RelatorioGeralController(RelatorioGeralService service) {
        this.service = service;
    }
    @GetMapping
    public ResponseEntity<List<VendaAutorDto> > getRelatorios() {

        List<VendaAutorDto> dadosViewRelatorioGeralAutor = service.consultar();

        return ResponseEntity.ok().body(dadosViewRelatorioGeralAutor);
    }

    @GetMapping(path = "/download"
            , produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerarRelatorio() throws JRException {

        List<VendaAutorDto> dadosViewRelatorioGeralAutor = service.consultar();

        byte[] pdfBytes = service.gerarRelatorioPDF(dadosViewRelatorioGeralAutor);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=relatorio.pdf")
                .body(pdfBytes);
    }

}
