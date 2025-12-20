package br.com.solucian.book.application.service;

import br.com.solucian.book.application.dto.AssuntoRequest;
import br.com.solucian.book.application.dto.AssuntoResponse;
import br.com.solucian.book.application.mapper.AssuntoMapper;
import br.com.solucian.book.domain.Assunto;
import br.com.solucian.book.infrastructure.repository.AssuntoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssuntoService {

    private final AssuntoRepository repository;
    private final AssuntoMapper mapper;

    public AssuntoService(AssuntoRepository repository, AssuntoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public AssuntoResponse criar(AssuntoRequest request) {
        Assunto assunto = repository.insertAssunto(request.descricao());
        return mapper.toResponse(assunto);
    }

    public AssuntoResponse atualizar(Integer id, AssuntoRequest request) {
        try {
            Assunto assuntoAtualizado = repository.updateAssunto(id, request.descricao());
            return mapper.toResponse(assuntoAtualizado);
        } catch (Exception e) {
            throw new EntityNotFoundException("Assunto não encontrado");
        }
    }

    public AssuntoResponse buscarPorId(Integer id) {
        Assunto assunto = repository.getAssunto(id)
                .orElseThrow(() -> new EntityNotFoundException("Assunto não encontrado"));
        return mapper.toResponse(assunto);
    }

    @Transactional(readOnly = true)
    public List<AssuntoResponse> listar(int page, int size) {
        return repository.listAssuntos(page, size)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void remover(Integer id) {
        repository.deleteAssunto(id);
    }
}
