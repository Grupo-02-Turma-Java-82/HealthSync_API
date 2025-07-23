package com.generation.fitness_backend.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.generation.fitness_backend.enums.DificuldadeExercicio;
import jakarta.persistence.*;
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

	//exercicio
	@OneToMany(mappedBy = "exercicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties("exercicio")
	private List<TreinoExercicio> treinoExercicios;

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

	public String getDescricaoDetalhada() {
		return descricaoDetalhada;
	}

	public void setDescricaoDetalhada(String descricaoDetalhada) {
		this.descricaoDetalhada = descricaoDetalhada;
	}

	public DificuldadeExercicio getNivelDificuldade() {
		return nivelDificuldade;
	}

	public void setNivelDificuldade(DificuldadeExercicio nivelDificuldade) {
		this.nivelDificuldade = nivelDificuldade;
	}

	public String getUrlVideoDemonstrativo() {
		return urlVideoDemonstrativo;
	}

	public void setUrlVideoDemonstrativo(String urlVideoDemonstrativo) {
		this.urlVideoDemonstrativo = urlVideoDemonstrativo;
	}

	public String getEquipamentoNecessario() {
		return equipamentoNecessario;
	}

	public void setEquipamentoNecessario(String equipamentoNecessario) {
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

	public List<TreinoExercicio> getTreinoExercicios() {
		return treinoExercicios;
	}

	public void setTreinoExercicios(List<TreinoExercicio> treinoExercicios) {
		this.treinoExercicios = treinoExercicios;
	}
}