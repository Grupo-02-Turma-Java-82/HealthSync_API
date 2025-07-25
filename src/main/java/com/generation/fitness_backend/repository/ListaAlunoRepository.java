package com.generation.fitness_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.fitness_backend.model.ListaAluno;

public interface ListaAlunoRepository extends JpaRepository<ListaAluno, Long> {
  List<ListaAluno> findByTreinadorId(Long treinadorId);
}
