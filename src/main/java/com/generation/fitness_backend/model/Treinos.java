package com.generation.fitness_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "tb_treinos")
public class Treinos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O atributo Nome é obrigatório!")
	@Column(nullable = false, length = 255)
	private String nome;

	@Size(max = 500)
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	@JsonIgnoreProperties({ "treinos", "dataCadastro", "dataDesativacao" })
	private Usuario usuario;

	@OneToMany(mappedBy = "treino", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties("treino")
	private List<TreinoExercicio> treinoExercicios;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<TreinoExercicio> getTreinoExercicios() {
		return treinoExercicios;
	}

	public void setTreinoExercicios(List<TreinoExercicio> treinoExercicios) {
		this.treinoExercicios = treinoExercicios;
	}
}
