package com.generation.fitness_backend.model;

import java.math.BigDecimal;
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

public class ExercicioRegistro {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // de integer para long
	private Long id;
	
    @ManyToMany(mappedBy = "tb_exercicios")
	@Column(name = "id_exercicio", nullable = false)
    private Set<Exercicios> exercicio = new HashSet<>();

    @ManyToMany(mappedBy = "tb_usuarios")
 	@Column(name = "id_usuario", nullable = false)
    private Set<Usuario> usuario = new HashSet<>();

	@NotNull(message = "A a duração dos exercicos é obrigatória")
	@Column(length = 100, nullable = false)
	private Integer duracao;

	@NotNull(message = "A quantidade de repeticao é obrigatória")
	@Column(length = 100, nullable = false)
	private Integer repeticao;

	@NotNull(message = "a carga é obrigatório")
	@Column(length = 100, nullable = false)
	private BigDecimal carga;

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

	public Integer getDuracao() {
		return duracao;
	}

	public void setDuracao(Integer duracao) {
		this.duracao = duracao;
	}

	public Integer getRepeticao() {
		return repeticao;
	}

	public void setRepeticao(Integer repeticao) {
		this.repeticao = repeticao;
	}

	public BigDecimal getCarga() {
		return carga;
	}

	public void setCarga(BigDecimal carga) {
		this.carga = carga;
	}

	public LocalDateTime getDataCadastro() {
		return dataCriacao;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCriacao = dataCadastro;
	}

}
