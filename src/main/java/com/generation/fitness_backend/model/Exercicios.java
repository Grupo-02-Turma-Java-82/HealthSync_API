package com.generation.fitness_backend.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_exercicios")
public class Exercicios {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O atributo nome é Obrigatório!")
	@Size(min = 5, max = 100, message = "O atributo nome deve conter no mínimo 05 e no máximo 100 caracteres")
	private String nome;

	@NotBlank(message = "O atributo descricao_detalhada é Obrigatório!")
	@Column(length = 100, nullable = false)
	@Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres.")
	private String descricaoDetalhada;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O nível de dificuldade do exercício é obrigatório!")
	@Column(name = "nivel_dificuldade", nullable = false, length = 50)
	private DificuldadeExercicio nivelDificuldade;

	@NotBlank(message = "O atributo url_video_demonstrativo é Obrigatório!")
	@Column(length = 100, nullable = false)
	@Size(max = 1000, message = "A url do vídeo deve ter no máximo 1000 caracteres.")
	private String urlVideoDemonstrativo;

	@Column(length = 100, nullable = true)
	@Size(max = 1000, message = "O atributo equipamento necessário deve ter no máximo 1000 caracteres.")
	private String equipamentoNecessario;

	@NotNull(message = "A data de criação é obrigatória!")
	@Column(name = "data_criacao", nullable = false)
	private LocalDate dataCriacao;

	@ManyToOne
	@JsonIgnoreProperties("exercicios")
	private Categoria categoria;

	public Exercicios() {
		this.dataCriacao = LocalDate.now();
	}

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

	public String getDescricao_detalhada() {
		return descricaoDetalhada;
	}

	public void setDescricao_detalhada(String descricaoDetalhada) {
		this.descricaoDetalhada = descricaoDetalhada;
	}

	public DificuldadeExercicio getNivel_dificuldade() {
		return nivelDificuldade;
	}

	public void setNivel_dificuldade(DificuldadeExercicio nivelDificuldade) {
		this.nivelDificuldade = nivelDificuldade;
	}

	public String getUrl_video_demonstrativo() {
		return urlVideoDemonstrativo;
	}

	public void setUrl_video_demonstrativo(String urlVideoDemonstrativo) {
		this.urlVideoDemonstrativo = urlVideoDemonstrativo;
	}

	public String getEquipamento_necessario() {
		return equipamentoNecessario;
	}

	public void setEquipamento_necessario(String equipamentoNecessario) {
		this.equipamentoNecessario = equipamentoNecessario;
	}

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}