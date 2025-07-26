package com.generation.fitness_backend.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "tb_treino_registro")
public class TreinoRegistro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_treino", nullable = false)
	@JsonIgnoreProperties("treinoRegistros")
	private Treinos treino;

	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	@JsonIgnoreProperties({"treinoRegistros", "exercicioRegistros", "pesos", "treinos", "atividadesRegistro", "listaAlunos"}) // Ajuste conforme as propriedades que causam loop em Usuario
	private Usuario usuario;

	@NotNull(message = "O nível de stress é obrigatória")
	@Column(nullable = false)
	private Integer nivelStress;

	@NotNull(message = "A data de início do treino é obrigatória!")
	@Column(nullable = false)
	private LocalDateTime dataInicio;

	@NotNull(message = "A data de término do treino é obrigatória!")
	@Column(nullable = false)
	private LocalDateTime dataTermino;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataCriacao;

	public TreinoRegistro() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Treinos getTreino() {
		return treino;
	}

	public void setTreino(Treinos treino) {
		this.treino = treino;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public @NotNull(message = "O nível de stress é obrigatória") Integer getNivelStress() {
		return nivelStress;
	}

	public void setNivelStress(@NotNull(message = "O nível de stress é obrigatória") Integer nivelStress) {
		this.nivelStress = nivelStress;
	}

	public @NotNull(message = "A data de início do treino é obrigatória!") LocalDateTime getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(@NotNull(message = "A data de início do treino é obrigatória!") LocalDateTime dataInicio) {
		this.dataInicio = dataInicio;
	}

	public @NotNull(message = "A data de término do treino é obrigatória!") LocalDateTime getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(@NotNull(message = "A data de término do treino é obrigatória!") LocalDateTime dataTermino) {
		this.dataTermino = dataTermino;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
}
