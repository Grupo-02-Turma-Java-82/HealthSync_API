package com.generation.fitness_backend.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.fitness_backend.model.Usuario;

public class UserDetailsImpl implements UserDetails { //MAPEAR EMAIL DO USUARIO PRA USERNAME""

	private static final long serialVersionUID = 1L;

	private String username; // representa email do usuario
	private String password;
	private List<GrantedAuthority> authorities;

	public UserDetailsImpl(Usuario user) {	//identificador unico
		
		//qd eu te der o user, vc pega o email
		this.username = user.getEmail(); // this username = chamar o email ((username))......!!
		this.password = user.getSenha();

	}

	public UserDetailsImpl() {
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return null;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() { 
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {

		return true;
	}
}