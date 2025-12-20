package br.com.solucian.book.infrastructure.repository;

import br.com.solucian.book.application.dto.AutorSimplesDTO;
import br.com.solucian.book.domain.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Integer> {

    @Query(value = "SELECT * FROM fn_autor_list(:page, :pageSize)", nativeQuery = true)
    List<Autor> listAutores(@Param("page") int page, @Param("pageSize") int pageSize);

    @Query(value = "SELECT * FROM fn_autor_get(:codau)", nativeQuery = true)
    Optional<Autor> getAutor(@Param("codau") Integer codau);

    @Query(value = "SELECT * FROM public.fn_autor_insert(:nome)", nativeQuery = true)
    Autor insertAutor(@Param("nome") String nome);

    @Query(value = "SELECT * FROM fn_autor_update(:codau, :nome)", nativeQuery = true)
    Autor updateAutor(@Param("codau") Integer codau, @Param("nome") String nome);

    @Query(value = "SELECT fn_autor_delete(:codau)", nativeQuery = true)
    Boolean deleteAutor(@Param("codau") Integer codau);

}
