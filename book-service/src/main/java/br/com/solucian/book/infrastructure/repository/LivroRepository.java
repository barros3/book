package br.com.solucian.book.infrastructure.repository;

import br.com.solucian.book.application.projection.AssuntoProjection;
import br.com.solucian.book.application.projection.AutorProjection;
import br.com.solucian.book.domain.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Integer> {

    @Query(
            value = """
            SELECT * FROM public.fn_livro_insert(
                :p_titulo,
                :p_editora,
                :p_edicao,
                :p_anopublicacao,
                :p_valor,
                CAST(:p_autores AS INTEGER[]),
                CAST(:p_assuntos AS INTEGER[])
            )
            """,
            nativeQuery = true
    )

    Livro create(
            @Param("p_titulo") String titulo,
            @Param("p_editora") String editora,
            @Param("p_edicao") Integer edicao,
            @Param("p_anopublicacao") String anopublicacao,
            @Param("p_valor") BigDecimal valor,
            @Param("p_autores") Integer[] autores,
            @Param("p_assuntos") Integer[] assuntos
    );

    @Query(
            value = """
            SELECT * FROM public.fn_livro_update(
                :codl,
                :titulo,
                :editora,
                :edicao,
                :anopublicacao,
                :valor,
                CAST(:autores AS INTEGER[]),
                CAST(:assuntos AS INTEGER[])
            )
        """,
            nativeQuery = true
    )
    Livro update(
            @Param("codl") Integer codl,
            @Param("titulo") String titulo,
            @Param("editora") String editora,
            @Param("edicao") Integer edicao,
            @Param("anopublicacao") String anopublicacao,
            @Param("valor") BigDecimal valor,
            @Param("autores") Integer[] autorId,
            @Param("assuntos") Integer[] assuntoId
    );

    @Query(value = "SELECT * FROM public.fn_livro_list(:page, :pageSize)", nativeQuery = true)
    List<Livro> listLivros(@Param("page") int page, @Param("pageSize") int pageSize);

    @Query(value = "SELECT public.fn_livro_delete(:codl) ", nativeQuery = true)
    void deleteLivro(@Param("codl") Integer codl);

    @Query(value = """
        SELECT a.codau as codau, a.nome as nome 
        FROM autor a 
        INNER JOIN livro_autor la ON a.codau = la.autor_codau 
        WHERE la.livro_cod = :livroId
        """, nativeQuery = true)
    List<AutorProjection> findAutoresByLivroIdProjection(@Param("livroId") Integer livroId);

    @Query(value = """
        SELECT asu.codas as codas, asu.descricao as descricao 
        FROM assunto asu 
        INNER JOIN livro_assunto las ON asu.codas = las.assunto_codas 
        WHERE las.livro_cod = :livroId
        """, nativeQuery = true)
    List<AssuntoProjection> findAssuntosByLivroIdProjection(@Param("livroId") Integer livroId);
}
