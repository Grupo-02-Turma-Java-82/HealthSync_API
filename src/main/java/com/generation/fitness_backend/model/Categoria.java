package com.generation.fitness_backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_categorias")
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_categoria")
	private Long id;

	@NotBlank(message = "O nome é obrigatório!")
	@Size(min = 3, max = 100, message = "O atributo nome dever ter no minimo 3 e no maximo 100 caracteres.")
	private String nome;

	@Size(max = 500)
	private String descricao;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "categoria", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("categoria")
	private List<Exercicios> exercicios;

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

	public List<Exercicios> getExercicios() {
		return exercicios;
	}

	public void setExercicios(List<Exercicios> exercicios) {
		this.exercicios = exercicios;
	}

}