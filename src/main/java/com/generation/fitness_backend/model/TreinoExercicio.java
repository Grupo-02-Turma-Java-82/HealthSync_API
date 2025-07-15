package com.generation.fitness_backend.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

public class TreinoExercicio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // de integer para long
	private Long id;

    @ManyToMany(mappedBy = "tb_exercicios")
	@Column(name = "id_exercicio", nullable = false)
    private Set<Exercicios> exercicio = new HashSet<>();

    @ManyToMany(mappedBy = "tb_treinos")
	@Column(name = "id_treino", nullable = false)
    private Set<Treinos> treino = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Exercicios> getExercicio() {
		return exercicio;
	}

	public void setExercicio(Set<Exercicios> exercicio) {
		this.exercicio = exercicio;
	}

	public Set<Treinos> getTreino() {
		return treino;
	}

	public void setTreino(Set<Treinos> treino) {
		this.treino = treino;
	}

}
