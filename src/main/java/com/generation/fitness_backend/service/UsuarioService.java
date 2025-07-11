package com.generation.fitness_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.model.UsuarioLogin;
import com.generation.fitness_backend.repository.UsuarioRepository;
import com.generation.fitness_backend.security.JwtService;

@Service
public class UsuarioService { //logica de autent. e criptografia de senha

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JwtService jwtService; //injetar jwt service

	@Autowired
	private AuthenticationManager authenticationManager;

	public List<Usuario> getAll() { //buscar todos
		return usuarioRepository.findAll();
	}

	public Optional<Usuario> getById(Long id) { // busca pelo ID
		return usuarioRepository.findById(id);
	}

	public Optional<Usuario> getByEmail(String email) { // busca pelo email
		return usuarioRepository.findByEmail(email);
	}

	public Optional<Usuario> cadastrarUsuario(Usuario usuario) { //cadastrar

		if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
			return Optional.empty();
		}

		usuario.setSenha(criptografarSenha(usuario.getSenha()));

		return Optional.of(usuarioRepository.save(usuario));
	}

	// MÉTODO CORRIGIDO
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {

		if (usuario.getId() == null || usuarioRepository.findById(usuario.getId()).isEmpty()) {
			return Optional.empty();
		}

		Optional<Usuario> buscaUsuarioPorEmail = usuarioRepository.findByEmail(usuario.getEmail());
		if (buscaUsuarioPorEmail.isPresent() && !buscaUsuarioPorEmail.get().getId().equals(usuario.getId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado por outro usuário!");
		}

		// CORREÇÃO: Busca o usuário do banco para atualizá-lo
		Usuario usuarioExistente = usuarioRepository.findById(usuario.getId()).get();

		// CORREÇÃO: Atualiza a senha apenas se uma nova for fornecida
		if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
			usuarioExistente.setSenha(criptografarSenha(usuario.getSenha()));
		}

		// CORREÇÃO: Copia todos os dados do objeto recebido para o objeto do banco
		usuarioExistente.setNomeCompleto(usuario.getNomeCompleto());
		usuarioExistente.setEmail(usuario.getEmail());
		usuarioExistente.setDataNascimento(usuario.getDataNascimento());
		usuarioExistente.setGenero(usuario.getGenero());
		usuarioExistente.setAlturaCm(usuario.getAlturaCm());
		usuarioExistente.setPesoKg(usuario.getPesoKg());
		usuarioExistente.setObjetivoPrincipal(usuario.getObjetivoPrincipal());
        // Se houver mais campos para atualizar, adicione-os aqui

		// CORREÇÃO: Salva o objeto que foi de fato atualizado
		return Optional.of(usuarioRepository.save(usuarioExistente));
	}

	// MÉTODO CORRIGIDO
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

		if (usuarioLogin.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenciais de login inválidas.");
		}

		var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getEmail(),
				usuarioLogin.get().getSenha());

		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(credenciais);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos!", e);
		}

		if (authentication.isAuthenticated()) {
			Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioLogin.get().getEmail());

			if (usuario.isPresent()) {
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNomeCompleto(usuario.get().getNomeCompleto());
				usuarioLogin.get().setEmail(usuario.get().getEmail());
				
                // CORREÇÃO: Adiciona a linha para retornar o tipo do usuário
				usuarioLogin.get().setTipoUsuario(usuario.get().getTipoUsuario());
				
                usuarioLogin.get().setSenha("");
				usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getEmail()));

				return usuarioLogin;
			}
		}

		return Optional.empty();
	}

	public void deleteById(Long id) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		if (usuario.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");
		}

		usuarioRepository.deleteById(id);
	}

	private String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);
	}

	private String gerarToken(String email) {
		return "Bearer " + jwtService.generateToken(email);
	}
}
