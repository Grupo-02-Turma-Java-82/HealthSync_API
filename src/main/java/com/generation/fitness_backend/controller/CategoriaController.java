package com.generation.fitness_backend.controller;

import java.util.List;

import com.generation.fitness_backend.model.Categoria;
import com.generation.fitness_backend.service.CategoriaService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Categorias", description = "Gerencia as categorias de produtos ou serviços na plataforma de fitness")
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;

	@GetMapping
	@SecurityRequirement(name = "jwt_auth")
	public ResponseEntity<List<Categoria>> getAll() {
		return ResponseEntity.ok(categoriaService.listarTodas());
	}

	@GetMapping("/{id}")
	@SecurityRequirement(name = "jwt_auth")
	public ResponseEntity<Categoria> getById(@PathVariable Long id) {
		return categoriaService.buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/nome/{nome}")
	@SecurityRequirement(name = "jwt_auth")
	public ResponseEntity<List<Categoria>> getByNome(@PathVariable String nome) {
		return ResponseEntity.ok(categoriaService.buscarPorNome(nome));
	}

	@PostMapping
	@SecurityRequirement(name = "jwt_auth")
	public ResponseEntity<Categoria> post(@Valid @RequestBody Categoria categoria) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.criarCategoria(categoria));
	}

	@PutMapping
	@SecurityRequirement(name = "jwt_auth")
	public ResponseEntity<Categoria> put(@Valid @RequestBody Categoria categoria) {
		return categoriaService.atualizarCategoria(categoria)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@DeleteMapping("/{id}")
	@SecurityRequirement(name = "jwt_auth")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		boolean apagado = categoriaService.deletarCategoria(id);
		if (!apagado)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}
}
