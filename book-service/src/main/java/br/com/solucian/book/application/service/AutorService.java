package br.com.solucian.book.application.service;

import br.com.solucian.book.application.dto.AutorRequest;
import br.com.solucian.book.application.dto.AutorResponse;
import br.com.solucian.book.application.mapper.AutorMapper;
import br.com.solucian.book.domain.Autor;
import br.com.solucian.book.infrastructure.repository.AutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AutorService {

    private final AutorRepository repository;
    private final AutorMapper mapper;

    public AutorService(AutorRepository repository, AutorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public AutorResponse criar(AutorRequest request) {
        Autor autor = repository.insertAutor(request.nome());
        return mapper.toResponse(autor);
    }

    public AutorResponse atualizar(Integer id, AutorRequest request) {
        Autor autor = repository.updateAutor(id, request.nome());
        return mapper.toResponse(autor);
    }

    @Transactional(readOnly = true)
    public AutorResponse buscarPorId(Integer id) {
        Autor autor = repository.getAutor(id).orElse(null);
        if (autor == null) {
            throw new EntityNotFoundException("Autor não encontrado");
        }
        return mapper.toResponse(autor);
    }

    @Transactional(readOnly = true)
    public List<AutorResponse> listar(int page, int size) {
        return repository.listAutores(page, size)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void remover(Integer id) {
        repository.deleteAutor(id);
    }

}
