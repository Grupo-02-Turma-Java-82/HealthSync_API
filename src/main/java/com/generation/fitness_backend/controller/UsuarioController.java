package com.generation.fitness_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.model.UsuarioLogin;
import com.generation.fitness_backend.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Usuários", description = "API para gerenciamento de usuários")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class, type = "array"))),
			@ApiResponse(responseCode = "401", description = "Não autorizado - Token JWT inválido ou ausente")
	})
	@GetMapping
	@SecurityRequirement(name = "jwt_auth")
	public ResponseEntity<List<Usuario>> getAll() {
		return ResponseEntity.ok(usuarioService.getAll());
	}

	@Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
			@ApiResponse(responseCode = "401", description = "Não autorizado - Token JWT inválido ou ausente")
	})
	@GetMapping("/{id}")
	@SecurityRequirement(name = "jwt_auth")
	public ResponseEntity<Usuario> getById(@PathVariable Long id) {
		return usuarioService.getById(id)
				.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@Operation(summary = "Buscar usuário por email", description = "Retorna um usuário específico com base no email fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado com este email"),
			@ApiResponse(responseCode = "401", description = "Não autorizado - Token JWT inválido ou ausente")
	})
	@GetMapping("/email/{email}")
	@SecurityRequirement(name = "jwt_auth")
	public ResponseEntity<Usuario> getByEmail(@PathVariable String email) {
		return usuarioService.getByEmail(email)
				.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@Operation(summary = "Cadastrar novo usuário", description = "Cria um novo usuário no sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
			@ApiResponse(responseCode = "400", description = "Requisição inválida - Email já cadastrado ou dados inválidos")
	})
	@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> post(@Valid @RequestBody Usuario usuario) {
		Optional<Usuario> novoUsuario = usuarioService.cadastrarUsuario(usuario);

		if (novoUsuario.isPresent()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario.get());
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este email já foi cadastrado!");
		}
	}

	@Operation(summary = "Atualizar usuário existente", description = "Atualiza os dados de um usuário existente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
			@ApiResponse(responseCode = "400", description = "Requisição inválida - ID do usuário não fornecido"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado para atualização"),
			@ApiResponse(responseCode = "401", description = "Não autorizado - Token JWT inválido ou ausente")
	})
	@PutMapping("/atualizar")
	@SecurityRequirement(name = "jwt_auth")
	public ResponseEntity<Usuario> put(@Valid @RequestBody Usuario usuario) {
		if (usuario.getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"O ID do usuário é obrigatório para a atualização...");
		}

		Optional<Usuario> usuarioAtualizado = usuarioService.atualizarUsuario(usuario);

		if (usuarioAtualizado.isPresent()) {
			return ResponseEntity.ok(usuarioAtualizado.get());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"O Usuário não foi encontrado para a atualização...");
		}
	}

	@Operation(summary = "Autenticar usuário (Login)", description = "Autentica um usuário e retorna um token JWT se as credenciais forem válidas.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioLogin.class))),
			@ApiResponse(responseCode = "401", description = "Não autorizado - Usuário ou senha inválidos")
	})
	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> logar(@RequestBody Optional<UsuarioLogin> usuarioLogin) {
		Optional<UsuarioLogin> usuarioAutenticado = usuarioService.autenticarUsuario(usuarioLogin);

		if (usuarioAutenticado.isPresent()) {
			return ResponseEntity.ok(usuarioAutenticado.get());
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos!");
		}
	}
}