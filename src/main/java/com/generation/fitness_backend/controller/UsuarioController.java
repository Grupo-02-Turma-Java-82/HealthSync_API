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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<List<Usuario>> getAll() {

		return ResponseEntity.ok(usuarioService.findAll()); // retorna todos os usuários com status 200 OK
	}

	@GetMapping("/{id}")
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
	public ResponseEntity<Usuario> post(@Valid @RequestBody Usuario usuario) {
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(usuario));
	}

	@PutMapping("/atualizar")
	public ResponseEntity<Usuario> put(@Valid @RequestBody Usuario usuario) {
		return ResponseEntity.ok(usuarioService.update(usuario));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		usuarioService.deleteById(id);
	}
}
