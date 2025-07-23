package com.generation.fitness_backend.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_treinoexercicio")
public class TreinoExercicio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_exercicio", nullable = false)
	@JsonIgnoreProperties("treinoExercicios")
	private Exercicios exercicio;

	@ManyToOne
	@JoinColumn(name = "id_treino", nullable = false)
	@JsonIgnoreProperties("treinoExercicios")
	private Treinos treino;

	public TreinoExercicio() {
	}

	public TreinoExercicio(Exercicios exercicio, Treinos treino) {
		this.exercicio = exercicio;
		this.treino = treino;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Exercicios getExercicio() {
		return exercicio;
	}

	public void setExercicio(Exercicios exercicio) {
		this.exercicio = exercicio;
	}

	public Treinos getTreino() {
		return treino;
	}

	public void setTreino(Treinos treino) {
		this.treino = treino;
	}
}