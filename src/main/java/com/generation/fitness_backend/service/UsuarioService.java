package com.generation.fitness_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	public Optional<Usuario> findById(Integer id) {
		return usuarioRepository.findById(id);
	}

	public Optional<Usuario> findByEmail(String email) {
		return usuarioRepository.findByEmailIgnoreCase(email);
	}

	public Usuario save(Usuario usuario) {
		if (usuarioRepository.findByEmailIgnoreCase(usuario.getEmail()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Email já está cadastrado!");
		}
		return usuarioRepository.save(usuario);
	}

	public Usuario update(Usuario usuario) {
		if (usuario.getIdUsuario() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do usuário é obrigatório para atualização!");
		}

		return usuarioRepository.findById(usuario.getIdUsuario()).map(usuarioExistente -> {
			usuarioExistente.setNomeCompleto(usuario.getNomeCompleto());
			usuarioExistente.setEmail(usuario.getEmail());
			usuarioExistente.setSenha(usuario.getSenha());
			usuarioExistente.setDataNascimento(usuario.getDataNascimento());
			usuarioExistente.setGenero(usuario.getGenero());
			usuarioExistente.setAlturaCm(usuario.getAlturaCm());
			usuarioExistente.setPesoKg(usuario.getPesoKg());
			usuarioExistente.setObjetivoPrincipal(usuario.getObjetivoPrincipal());

			return usuarioRepository.save(usuarioExistente);
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!"));
	}

	public void deleteById(Integer id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);

		if (usuario.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");
		}

		usuarioRepository.deleteById(id);
	}
}
