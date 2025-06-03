package com.generation.fitness_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.fitness_backend.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
	 Optional<Usuario> findByEmail(String email); // procurar usuario por email
	
}
