package br.com.solucian.book.application.mapper;

import br.com.solucian.book.application.dto.AutorResponse;
import br.com.solucian.book.domain.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AutorMapper {

    @Mapping(target = "codAu", expression = "java(autor.getCodAu())")
    @Mapping(target = "nome", expression = "java(autor.getNome())")
    @Mapping(target = "livros", ignore = true)
    AutorResponse toResponse(Autor autor);
}
