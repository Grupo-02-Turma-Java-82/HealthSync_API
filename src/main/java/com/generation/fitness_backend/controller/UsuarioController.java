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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController { //delegar chamada pra usuarioservice nova

	@Autowired
	private UsuarioService usuarioService; 

	@GetMapping
	public ResponseEntity<List<Usuario>> getAll() {
		return ResponseEntity.ok(usuarioService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getById(@PathVariable Long id) { // Long
		return usuarioService.getById(id)
				.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/email/{email}") // buscar por email
	public ResponseEntity<Usuario> getByEmail(@PathVariable String email) {
		return usuarioService.getByEmail(email)
				.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> post(@Valid @RequestBody Usuario usuario) {
		Optional<Usuario> novoUsuario = usuarioService.cadastrarUsuario(usuario);

		if (novoUsuario.isPresent()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario.get());
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este email já foi cadastrado!");
		}
	}

	@PutMapping("/atualizar")
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