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

import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.repository.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping
	public ResponseEntity<List<Usuario>> getAll() {
		
		return ResponseEntity.ok(usuarioRepository.findAll()); // retorna todos os usuários com status 200 OK
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getById(@PathVariable Integer id) {
		
		// usa findById do rep, que retorna um Optional
		return usuarioRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta)) // encontrou, retorna 200
				.orElse(ResponseEntity.notFound().build()); // nao encontrou, retorna 404
	}

	@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> post(@Valid @RequestBody Usuario usuario) {

		try {
			Usuario novoUsuario = usuarioRepository.save(usuario);
			return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
		} catch (Exception e) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado ou dados inválidos!");
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<Usuario> put(@Valid @RequestBody Usuario usuario) {

		if (usuario.getIdUsuario() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do usuário é obrigatório para atualização!");
		}

		return usuarioRepository.findById(usuario.getIdUsuario()).map(resposta -> {

			resposta.setNomeCompleto(usuario.getNomeCompleto());
			resposta.setEmail(usuario.getEmail());
			resposta.setSenha(usuario.getSenha());
			resposta.setDataNascimento(usuario.getDataNascimento());
			resposta.setGenero(usuario.getGenero());
			resposta.setAlturaCm(usuario.getAlturaCm());
			resposta.setPesoKg(usuario.getPesoKg());
			resposta.setObjetivoPrincipal(usuario.getObjetivoPrincipal());

			Usuario usuarioAtualizado = usuarioRepository.save(resposta);
			return ResponseEntity.ok(usuarioAtualizado);
		}).orElse(ResponseEntity.notFound().build()); // se o usuário n existir, retorna 404
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);

		if (usuario.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");

		usuarioRepository.deleteById(id);

	}
}
