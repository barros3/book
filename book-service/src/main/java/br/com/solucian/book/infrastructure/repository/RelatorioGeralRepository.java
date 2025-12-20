package br.com.solucian.book.infrastructure.repository;

import br.com.solucian.book.application.projection.RelatorioGeralProjection;
import br.com.solucian.book.domain.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@org.springframework.stereotype.Repository
public interface RelatorioGeralRepository extends JpaRepository<Livro, Integer> {

    @Query(value = """
            SELECT
                autor_codau    AS autorCodau,
                autor_nome     AS autorNome,
                assuntos       AS assuntos,
                livros         AS livros,
                qtd_livros     AS qtdLivros,
                qtd_assuntos   AS qtdAssuntos,
                valor_total    AS valorTotal,
                atualizado_em  AS atualizadoEm
            FROM public.vw_relatorio_geral_autor
        """, nativeQuery = true)
    List<RelatorioGeralProjection> consultar();

}
