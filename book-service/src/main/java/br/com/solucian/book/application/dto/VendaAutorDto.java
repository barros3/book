package br.com.solucian.book.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public record VendaAutorDto(
        String codigo,
        String autor,
        String assuntos,
        String livros,
        Integer qtdLivros,
        Integer qtdAssuntos,
        BigDecimal vendaTotal,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        Date dataAtualizacao

) {

    public String getCodigo() {
        return codigo;
    }
    public String getAutor() {
        return autor;
    }
    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }
    public BigDecimal getVendaTotal() {
        return vendaTotal;
    }


    public String getAssuntos() {
        return assuntos;
    }

    public String getLivros() {
        return livros;
    }

    public Integer getQtdLivros() {
        return qtdLivros;
    }

    public Integer getQtdAssuntos() {
        return qtdAssuntos;
    }

}
