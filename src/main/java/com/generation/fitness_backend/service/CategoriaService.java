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

    // Buscar todas as categorias
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    // Buscar por ID
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    // Buscar por nome
    public List<Categoria> buscarPorNome(String nome) {
        return categoriaRepository.findAllByNomeContainingIgnoreCase(nome);
    }

    // Criar nova categoria
    public Categoria criarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // Atualizar categoria existente
    public Optional<Categoria> atualizarCategoria(Categoria categoria) {
        if (categoria.getId() != null && categoriaRepository.existsById(categoria.getId())) {
            return Optional.of(categoriaRepository.save(categoria));
        }
        return Optional.empty();
    }

    // Deletar por ID
    public boolean deletarCategoria(Long id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}