package com.generation.fitness_backend.service;

import com.generation.fitness_backend.model.Categoria;
import com.generation.fitness_backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Categoria criarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> atualizarCategoria(Categoria categoria) {
        if (categoria.getId() != null && categoriaRepository.existsById(categoria.getId())) {
            return Optional.of(categoriaRepository.save(categoria));
        }
        return Optional.empty();
    }

    public boolean deletarCategoria(Long id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}