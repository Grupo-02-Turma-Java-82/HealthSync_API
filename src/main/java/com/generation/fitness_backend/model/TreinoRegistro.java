package com.generation.fitness_backend.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;

public class TreinoRegistro {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // de integer para long
	private Long id;
	
    @ManyToMany(mappedBy = "tb_exercicios")
	@Column(name = "id_exercicio", nullable = false)
    private Set<Exercicios> exercicio = new HashSet<>();

    @ManyToMany(mappedBy = "tb_usuarios")
 	@Column(name = "id_usuario", nullable = false)
    private Set<Usuario> usuario = new HashSet<>();

	@NotNull(message = "O nível de stress é obrigatória")
	@Column(length = 100, nullable = false)
	private Integer nivelStress;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataInicio;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataTermino;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataCriacao;

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

	public Set<Usuario> getUsuario() {
		return usuario;
	}

	public void setUsuario(Set<Usuario> usuario) {
		this.usuario = usuario;
	}

	public LocalDateTime getDataCadastro() {
		return dataCriacao;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCriacao = dataCadastro;
	}

	public Integer getNivelStress() {
		return nivelStress;
	}

	public void setNivelStress(Integer nivelStress) {
		this.nivelStress = nivelStress;
	}

	public LocalDateTime getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDateTime dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDateTime getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(LocalDateTime dataTermino) {
		this.dataTermino = dataTermino;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

}
