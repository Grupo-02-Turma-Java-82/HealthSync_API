package com.generation.fitness_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.fitness_backend.model.Exercicios;
import com.generation.fitness_backend.repository.ExerciciosRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/exercicios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ExerciciosController {

	@Autowired
	private ExerciciosRepository exercicosRepository;

	@GetMapping
	public ResponseEntity<List<Exercicios>> getAll() {
		return ResponseEntity.ok(exercicosRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Exercicios> getById(@PathVariable Long id) {
		return exercicosRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping
	public ResponseEntity<Exercicios> post(@Valid @RequestBody Exercicios exercicio) {
		// validaçao na camada de servico!!
		return ResponseEntity.status(HttpStatus.CREATED).body(exercicosRepository.save(exercicio));
	}

	@PutMapping
	public ResponseEntity<Exercicios> put(@Valid @RequestBody Exercicios exercicio) {

		if (exercicio.getId() == null) {
			return ResponseEntity.badRequest().build(); // ID é obrigatório para PUT
		}

		if (exercicosRepository.existsById(exercicio.getId())) {
			return ResponseEntity.status(HttpStatus.OK).body(exercicosRepository.save(exercicio));
		}

		return ResponseEntity.status(HttpStatus.OK).body(exercicosRepository.save(exercicio));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Exercicios> exercicio = exercicosRepository.findById(id);

		if(exercicio.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Oportunidade não encontrada!");
		}

		exercicosRepository.deleteById(id);
	}
}
