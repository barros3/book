package br.com.solucian.book.application.mapper;

import br.com.solucian.book.application.dto.AssuntoResponse;
import br.com.solucian.book.domain.Assunto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AssuntoMapper {

    @Mapping(target = "codAs", expression = "java(assunto.getCodAs())")
    @Mapping(target = "descricao", expression = "java(assunto.getDescricao())")
    @Mapping(target = "livros", ignore = true)
    AssuntoResponse toResponse(Assunto assunto);

}
