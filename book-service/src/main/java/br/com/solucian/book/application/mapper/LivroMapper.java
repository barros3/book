package br.com.solucian.book.application.mapper;

import br.com.solucian.book.application.dto.LivroResponse;
import br.com.solucian.book.domain.Livro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LivroMapper {

    @Mapping(target = "codl", expression = "java(livro.getCodl())")
    @Mapping(target = "titulo", expression = "java(livro.getTitulo())")
    @Mapping(target = "editora", expression = "java(livro.getEditora())")
    @Mapping(target = "edicao", expression = "java(livro.getEdicao())")
    @Mapping(target = "anoPublicacao", expression = "java(livro.getAnoPublicacao())")
    @Mapping(target = "valor", expression = "java(livro.getValor())")
    @Mapping(target = "autores", ignore = true)
    @Mapping(target = "assuntos", ignore = true)
    LivroResponse toResponse(Livro livro);

    LivroResponse toResponseDetail(Livro livro);
}
