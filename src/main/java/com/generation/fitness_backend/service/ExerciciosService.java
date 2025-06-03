package com.generation.fitness_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.fitness_backend.model.Exercicios;
import com.generation.fitness_backend.repository.ExerciciosRepository;

@Service
public class ExerciciosService {

	@Autowired
	private ExerciciosRepository exerciciosRepository;

	public List<Exercicios> findAll() {
		return exerciciosRepository.findAll();
	}

	public Optional<Exercicios> findById(Long id) {
		return exerciciosRepository.findById(id);
	}

	public List<Exercicios> findByNome(String nome) {
		return exerciciosRepository.findAllByNomeContainingIgnoreCase(nome);

	}

	public Exercicios criar(Exercicios exercicio) {
		return exerciciosRepository.save(exercicio);
	}

	public Optional<Exercicios> atualizar(Exercicios exercicio) {
		if (exercicio.getId() == null || !exerciciosRepository.existsById(exercicio.getId())) {
			return Optional.empty();
		}
		return Optional.of(exerciciosRepository.save(exercicio));
	}

	public void deleteById(Long id) {
		Optional<Exercicios> exercicio = exerciciosRepository.findById(id);

		if (exercicio.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");
		}

		exerciciosRepository.deleteById(id);
	}
}