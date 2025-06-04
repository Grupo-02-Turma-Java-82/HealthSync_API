package com.generation.fitness_backend.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	//email chega como username<< pra buscar usuario na resporitory

	@Autowired 
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// CAMPO DE LOGIN É EMAIL! passa username como email
		Optional<Usuario> usuario = usuarioRepository.findByEmail(username); 

		if (usuario.isPresent()) { 
			// encontrou user > cria instancia com os dados
			return new UserDetailsImpl(usuario.get());
			
		} else {
			
			throw new UsernameNotFoundException("Usuário não encontrado!");
		}
	}
}