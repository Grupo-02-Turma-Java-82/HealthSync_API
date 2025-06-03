package com.generation.fitness_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.generation.fitness_backend.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findAllByNomeContainingIgnoreCase(String nome);
}
