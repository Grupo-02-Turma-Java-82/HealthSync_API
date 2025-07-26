package com.generation.fitness_backend.service;

import com.generation.fitness_backend.model.Categoria;
import com.generation.fitness_backend.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public List<Categoria> buscarPorNome(String nome) {
        return categoriaRepository.findAllByNomeContainingIgnoreCase(nome);
    }

    @Transactional
    public Categoria criarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria atualizarCategoria(Categoria categoria) {
        return categoriaRepository.findById(categoria.getId())
                .map(categoriaExistente -> {
                    categoriaExistente.setNome(categoria.getNome());
                    categoriaExistente.setDescricao(categoria.getDescricao());
                    return categoriaRepository.save(categoriaExistente);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada para atualização!"));
    }
    @Transactional
    public void deletarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada para exclusão!");
        }
        categoriaRepository.deleteById(id);
    }
}