package com.generation.fitness_backend.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_lista_aluno")
public class ListaAluno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // de integer para long
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_aluno", nullable = false)
	@JsonIgnoreProperties({ "nomeCompleto", "urlImagem", "email", "senha", "dataNascimento", "genero", "alturaCm",
			"pesoKg", "objetivoPrincipal", "tipoUsuario", "ativo", "dataCadastro", "dataDesativacao" })
	private Usuario aluno;

	@ManyToOne
	@JoinColumn(name = "id_treinador", nullable = false)
	@JsonIgnoreProperties({ "nomeCompleto", "urlImagem", "email", "senha", "dataNascimento", "genero", "alturaCm",
			"pesoKg", "objetivoPrincipal", "tipoUsuario", "ativo", "dataCadastro", "dataDesativacao" })
	private Usuario treinador;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataVinculo;

	public Usuario getAluno() {
		return aluno;
	}

	public void setAluno(Usuario aluno) {
		this.aluno = aluno;
	}

	public LocalDateTime getDataVinculo() {
		return this.dataVinculo;
	}

	public void setDataVinculo(LocalDateTime dataVinculo) {
		this.dataVinculo = dataVinculo;
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
