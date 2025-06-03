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

import com.generation.fitness_backend.model.Categoria;
import com.generation.fitness_backend.repository.CategoriaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Categorias", description = "Gerencia as categorias de produtos ou serviços na plataforma de fitness") // Anotação a nível de classe
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	@Operation(summary = "Lista todas as categorias", description = "Retorna uma lista completa de todas as categorias cadastradas.")
	@ApiResponse(responseCode = "200", description = "Lista de categorias obtida com sucesso.")
	public ResponseEntity<List<Categoria>> getAll() {
		return ResponseEntity.ok(categoriaRepository.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca categoria por ID", description = "Retorna uma categoria específica com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Categoria encontrada.", content = @Content(schema = @Schema(implementation = Categoria.class))),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada.")
	})
	public ResponseEntity<Categoria> getById(@PathVariable Long id) {
		return categoriaRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/nome/{nome}")
	@Operation(summary = "Busca categorias por nome", description = "Retorna uma lista de categorias cujo nome contém o texto fornecido (case-insensitive).")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de categorias por nome obtida com sucesso."),
			@ApiResponse(responseCode = "204", description = "Nenhuma categoria encontrada com o nome especificado.") // Adicionado para indicar No Content
	})
	public ResponseEntity<List<Categoria>> getByNome(@PathVariable String nome) {
		// Melhoria: Retornar NO_CONTENT se a lista estiver vazia para ser mais RESTful
		List<Categoria> categorias = categoriaRepository.findAllByNomeContainingIgnoreCase(nome);
		if (categorias.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(categorias);
	}

	@PostMapping
	@Operation(summary = "Cadastra uma nova categoria", description = "Cria uma nova categoria no sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Categoria cadastrada com sucesso.", content = @Content(schema = @Schema(implementation = Categoria.class))),
			@ApiResponse(responseCode = "400", description = "Dados da categoria inválidos.")
	})
	public ResponseEntity<Categoria> post(@Valid @RequestBody Categoria categoria) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRepository.save(categoria));
	}

	@PutMapping
	@Operation(summary = "Atualiza uma categoria existente", description = "Modifica os dados de uma categoria já cadastrada. O ID da categoria é obrigatório no corpo da requisição.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso.", content = @Content(schema = @Schema(implementation = Categoria.class))),
			@ApiResponse(responseCode = "400", description = "ID da categoria não fornecido ou inválido."),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada para atualização.")
	})
	public ResponseEntity<Categoria> put(@Valid @RequestBody Categoria categoria) {
		// Garante que o ID exista antes de tentar atualizar
		return categoriaRepository.findById(categoria.getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(categoriaRepository.save(categoria)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Deleta uma categoria por ID", description = "Remove uma categoria do sistema com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso."),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada para exclusão.")
	})
	public void delete(@PathVariable Long id) {
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		if (categoria.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada!"); // Adiciona mensagem
		categoriaRepository.deleteById(id);
	}
}