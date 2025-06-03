package com.generation.fitness_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.fitness_backend.model.Exercicios;

public interface ExerciciosRepository extends JpaRepository<Exercicios, Long> {
	List<Exercicios> findAllByNomeContainingIgnoreCase(String nome);
}
