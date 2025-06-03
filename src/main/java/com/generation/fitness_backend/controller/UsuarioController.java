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

import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Usuários", description = "Gerencia o cadastro e as informações dos usuários na plataforma de fitness")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	@Operation(summary = "Lista todos os usuários", description = "Retorna uma lista completa de todos os usuários cadastrados no sistema.")
	@ApiResponse(responseCode = "200", description = "Lista de usuários obtida com sucesso.")
	public ResponseEntity<List<Usuario>> getAll() {

		return ResponseEntity.ok(usuarioService.findAll()); // retorna todos os usuários com status 200 OK
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca usuário por ID", description = "Retorna os detalhes de um usuário específico com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário encontrado.", content = @Content(schema = @Schema(implementation = Usuario.class))),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
	})
	public ResponseEntity<Usuario> getById(@PathVariable Integer id) {
		// usa findById do rep, que retorna um Optional
		return usuarioService.findById(id)
				.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// getmapping para buscar por email
	@GetMapping("/email/{email}")
	public ResponseEntity<Usuario> getByEmail(@PathVariable String email) {
		return usuarioService.findByEmail(email)
				.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/cadastrar")
	@Operation(summary = "Cadastra um novo usuário", description = "Cria um novo usuário no sistema. O e-mail deve ser único.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso.", content = @Content(schema = @Schema(implementation = Usuario.class))),
			@ApiResponse(responseCode = "400", description = "E-mail já cadastrado ou dados do usuário inválidos.")
	})
	public ResponseEntity<Usuario> post(@Valid @RequestBody Usuario usuario) {

		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(usuario));
	}

	@PutMapping("/atualizar")
	@Operation(summary = "Atualiza um usuário existente", description = "Modifica os dados de um usuário já cadastrado. O ID do usuário é obrigatório no corpo da requisição e deve existir.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso.", content = @Content(schema = @Schema(implementation = Usuario.class))),
			@ApiResponse(responseCode = "400", description = "ID do usuário é obrigatório ou inválido na requisição."),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado para atualização.")
	})
	public ResponseEntity<Usuario> put(@Valid @RequestBody Usuario usuario) {

		return ResponseEntity.ok(usuarioService.update(usuario));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Deleta um usuário por ID", description = "Remove um usuário do sistema com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado para exclusão.")
	})
	public void delete(@PathVariable Integer id) {

		usuarioService.deleteById(id);
	}
}
