package br.com.solucian.book.application.service;

import br.com.solucian.book.application.dto.*;
import br.com.solucian.book.application.mapper.LivroMapper;
import br.com.solucian.book.domain.Livro;
import br.com.solucian.book.infrastructure.repository.AssuntoRepository;
import br.com.solucian.book.infrastructure.repository.AutorRepository;
import br.com.solucian.book.infrastructure.repository.LivroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LivroService {

    private final LivroRepository repository;
    private final AutorRepository autorRepository;
    private final AssuntoRepository assuntoRepository;
    private final LivroMapper mapper;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public LivroService(LivroRepository repository,
                        AutorRepository autorRepository,
                        AssuntoRepository assuntoRepository,
                        LivroMapper mapper) {
        this.repository = repository;
        this.autorRepository = autorRepository;
        this.assuntoRepository = assuntoRepository;
        this.mapper = mapper;
    }

    public LivroResponse criar(LivroRequest request) {
        Livro livro = repository.create(
                request.titulo(),
                request.editora(),
                request.edicao(),
                request.anoPublicacao(),
                request.valor(),
                request.autoresIds().toArray(Integer[]::new),
                request.assuntosIds().toArray(Integer[]::new)
        );
        return mapper.toResponse(livro);
    }

    public LivroResponse atualizar(Integer id, LivroCreateRequest request) {
        Livro livro = repository.update(
                id,
                request.titulo(),
                request.editora(),
                request.edicao(),
                request.anoPublicacao(),
                request.valor(),
                request.autoresIds().toArray(Integer[]::new),
                request.assuntosIds().toArray(Integer[]::new)
        );
        return mapper.toResponse(livro);
    }

    @Transactional(readOnly = true)
    public LivroResponseDetail buscarPorId(Integer id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado"));

        List<AutorSimplesDTO> autores = livroRepository.findAutoresByLivroIdProjection(id)
                .stream()
                .map(proj -> AutorSimplesDTO.builder()
                        .codAu(proj.getCodau())
                        .nome(proj.getNome())
                        .build())
                .collect(Collectors.toList());

        List<AssuntoSimplesDTO> assuntos = livroRepository.findAssuntosByLivroIdProjection(id)
                .stream()
                .map(proj -> AssuntoSimplesDTO.builder()
                        .codAs(proj.getCodas())
                        .descricao(proj.getDescricao())
                        .build())
                .collect(Collectors.toList());

        return LivroResponseDetail.builder()
                .codL(livro.getCodl())
                .titulo(livro.getTitulo())
                .editora(livro.getEditora())
                .edicao(livro.getEdicao())
                .anoPublicacao(livro.getAnoPublicacao())
                .valor(livro.getValor())
                .autores(autores)
                .assuntos(assuntos)
                .build();
    }
    @Transactional(readOnly = true)
    public List<LivroResponse> listar(int page, int size) {
        return repository.listLivros(page, size)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void remover(Integer id) {
        repository.deleteLivro(id);
    }
}
