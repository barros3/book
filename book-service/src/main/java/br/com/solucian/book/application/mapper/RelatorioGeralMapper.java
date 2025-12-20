package br.com.solucian.book.application.mapper;

import br.com.solucian.book.application.dto.RelatorioGeralResponse;
import br.com.solucian.book.application.dto.VendaAutorDto;
import br.com.solucian.book.application.projection.RelatorioGeralProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RelatorioGeralMapper {

    List<RelatorioGeralResponse> toResponseList(List<RelatorioGeralProjection> entities);

    @Mapping(target = "codigo", expression = "java(String.valueOf(p.getAutorCodau()))")
    @Mapping(target = "autor", source = "autorNome")
    @Mapping(target = "vendaTotal", source = "valorTotal")
    @Mapping(target = "dataAtualizacao", source = "atualizadoEm", dateFormat = "dd/MM/yyyy HH:mm:ss")
    VendaAutorDto map(RelatorioGeralProjection p);

    List<VendaAutorDto> toResponseVenda(List<RelatorioGeralProjection> entities);

}
