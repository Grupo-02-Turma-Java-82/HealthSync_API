package com.generation.fitness_backend.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_lista_aluno")
public class ListaAluno {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // de integer para long
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_aluno", nullable = false)
	private Usuario aluno;

	@ManyToOne
	@JoinColumn(name = "id_treinador", nullable = false)
	private Usuario treinador;

	public Usuario getAluno() {
		return aluno;
	}

	public void setAluno(Usuario aluno) {
		this.aluno = aluno;
	}

	public Usuario getTreinador() {
		return treinador;
	}

	public void setTreinador(Usuario treinador) {
		this.treinador = treinador;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
