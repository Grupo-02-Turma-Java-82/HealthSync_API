package com.generation.fitness_backend.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_lista_aluno")
public class ListaAluno {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // de integer para long
	private Long id;

    @ManyToMany(mappedBy = "tb_usuarios")
	@Column(name = "id_aluno", nullable = false)
    private Set<Usuario> aluno = new HashSet<>();

    @ManyToMany(mappedBy = "tb_usuarios")
	@Column(name = "id_treinador", nullable = false)
    private Set<Usuario> treinador = new HashSet<>();

	public Set<Usuario> getAluno() {
		return aluno;
	}

	public void setAluno(Set<Usuario> aluno) {
		this.aluno = aluno;
	}

	public Set<Usuario> getTreinador() {
		return treinador;
	}

	public void setTreinador(Set<Usuario> treinador) {
		this.treinador = treinador;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
