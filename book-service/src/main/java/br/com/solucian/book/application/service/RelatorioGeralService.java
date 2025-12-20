package br.com.solucian.book.application.service;

import br.com.solucian.book.api.exceptions.RelatorioException;
import br.com.solucian.book.application.dto.VendaAutorDto;
import br.com.solucian.book.application.mapper.RelatorioGeralMapper;
import br.com.solucian.book.infrastructure.repository.RelatorioGeralRepository;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class RelatorioGeralService {

    private final RelatorioGeralRepository repository;
    private final RelatorioGeralMapper mapper;

    public RelatorioGeralService(RelatorioGeralRepository repository, RelatorioGeralMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<VendaAutorDto> consultar() {
        var rows = repository.consultar();
        return mapper.toResponseVenda(rows);
    }

    public byte[] gerarRelatorioPDF(List<VendaAutorDto> vendasPorAutor) throws JRException {
        InputStream jasperStream = this.getClass()
                .getResourceAsStream("/relatorios/relatorio-geral-autor.jasper");

        if (jasperStream == null) {
            throw new RelatorioException("Arquivo de relatório não encontrado");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("vendasPorAutor", vendasPorAutor);
        JRDataSource dataSource = new JREmptyDataSource();

        JasperPrint jasperPrint = JasperFillManager
                .fillReport(jasperStream, params, dataSource);

        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
        return pdfBytes;
    }

}
