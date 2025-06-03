package com.generation.fitness_backend.controller;

import java.util.List;

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
import com.generation.fitness_backend.service.ExerciciosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/exercicios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Exercícios", description = "Gerencia os exercícios cadastrados na plataforma de fitness")
public class ExerciciosController {

	@Autowired
	private ExerciciosService exerciciosService;

	@Autowired
	private ExerciciosRepository exerciciosRepository;

	@GetMapping
	@Operation(summary = "Lista todos os exercícios", description = "Retorna uma lista completa de todos os exercícios cadastrados.")
	@ApiResponse(responseCode = "200", description = "Lista de exercícios obtida com sucesso.")
	public ResponseEntity<List<Exercicios>> getAll() {
		return ResponseEntity.ok(exerciciosService.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca exercício por ID", description = "Retorna um exercício específico com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Exercício encontrado.", content = @Content(schema = @Schema(implementation = Exercicios.class))),
			@ApiResponse(responseCode = "404", description = "Exercício não encontrado.")
	})
	public ResponseEntity<Exercicios> getById(@PathVariable Long id) {

		return exerciciosService.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping
	@Operation(summary = "Cadastra um novo exercício", description = "Cria um novo exercício no sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Exercício cadastrado com sucesso.", content = @Content(schema = @Schema(implementation = Exercicios.class))),
			@ApiResponse(responseCode = "400", description = "Dados do exercício inválidos.")
	})
	public ResponseEntity<Exercicios> post(@Valid @RequestBody Exercicios exercicio) {
		return ResponseEntity.status(HttpStatus.CREATED).body(exerciciosService.criar(exercicio));
	}

	@PutMapping
	@Operation(summary = "Atualiza um exercício existente", description = "Modifica os dados de um exercício já cadastrado. O ID do exercício é obrigatório no corpo da requisição.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Exercício atualizado com sucesso.", content = @Content(schema = @Schema(implementation = Exercicios.class))),
			@ApiResponse(responseCode = "400", description = "ID do exercício não fornecido ou inválido."),
			@ApiResponse(responseCode = "404", description = "Exercício não encontrado para atualização.")
	})
	public ResponseEntity<Exercicios> put(@Valid @RequestBody Exercicios exercicio) {

		if (exercicio.getId() == null) {
			return ResponseEntity.badRequest().build(); // ID é obrigatório para PUT
		}

		// Melhoria: Retornar 404 se o exercício não existir
		if (!exerciciosRepository.existsById(exercicio.getId())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercício não encontrado para atualização!");
		}

		return ResponseEntity.status(HttpStatus.OK).body(exerciciosService.criar(exercicio));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // Para indicar que a operação foi bem-sucedida sem conteúdo de retorno
	@Operation(summary = "Deleta um exercício por ID", description = "Remove um exercício do sistema com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Exercício deletado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Exercício não encontrado para exclusão.")
	})
	public void delete(@PathVariable Long id) {

		exerciciosService.deleteById(id);
	}
}