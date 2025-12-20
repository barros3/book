package br.com.solucian.book.infrastructure.repository;

import br.com.solucian.book.application.dto.AssuntoSimplesDTO;
import br.com.solucian.book.domain.Assunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssuntoRepository extends JpaRepository<Assunto, Integer> {

    @Query(value = "SELECT * FROM fn_assunto_list(:page, :pageSize)", nativeQuery = true)
    List<Assunto> listAssuntos(@Param("page") int page, @Param("pageSize") int pageSize);

    @Query(value = "SELECT COUNT(*) FROM assunto", nativeQuery = true)
    int countTotalAssuntos();

    @Query(value = "SELECT * FROM fn_assunto_get(:codas)", nativeQuery = true)
    Optional<Assunto> getAssunto(@Param("codas") Integer codas);

    @Query(value = "SELECT * FROM fn_assunto_insert(:descricao)", nativeQuery = true)
    Assunto insertAssunto(@Param("descricao") String descricao);

    @Query(value = "SELECT * FROM fn_assunto_update(:codas, :descricao)", nativeQuery = true)
    Assunto updateAssunto(
            @Param("codas") Integer codas,
            @Param("descricao") String descricao
    );

    @Query(value = "SELECT fn_assunto_delete(:codas)", nativeQuery = true)
    Boolean deleteAssunto(@Param("codas") Integer codas);

}
