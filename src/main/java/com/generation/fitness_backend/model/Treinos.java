package com.generation.fitness_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_treinos")
public class Treinos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // de integer para long
	private Long id;

	@NotBlank(message = "O atributo Nome é obrigatório!")
	@Column(nullable = false, length = 255)
	private String nome;
	
	@Size(max = 500)
	private String descricao;

	@ManyToOne //muitos treinos para um usuario
	@JoinColumn(name = "id_usuario", nullable = false) //coluna da chave estrangeira na tabela tb_treinos
	@JsonIgnoreProperties({"treinos", "dataCadastro", "dataDesativacao"}) //ignora a lista de treinos em usuario para evitar recursao infinita
	private Usuario usuario;

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

}
