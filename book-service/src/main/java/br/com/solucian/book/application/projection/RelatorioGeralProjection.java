package br.com.solucian.book.application.projection;

import java.math.BigDecimal;
import java.util.Date;

public interface RelatorioGeralProjection {

    Integer getAutorCodau();
    String getAutorNome();
    String getAssuntos();
    String getLivros();
    Long getQtdLivros();
    Integer getQtdAssuntos();
    BigDecimal getValorTotal();
    Date getAtualizadoEm();
}