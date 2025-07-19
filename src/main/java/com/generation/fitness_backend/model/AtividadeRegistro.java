package com.generation.fitness_backend.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_atividade_registro")
public class AtividadeRegistro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	@JsonIgnoreProperties({ "atividadesRegistro", "pesos", "treinos" })
	private Usuario usuario;

	@NotBlank(message = "O atributo Título é obrigatório!")
	@Column(nullable = false, length = 255)
	private String titulo;

	@Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
	private String descricao;

	@Column(nullable = false, length = 1000)
	@Size(max = 1000, message = "A url da imagem deve ter no máximo 1000 caracteres.")
	private String urlImagem;

	@Column(nullable = false, length = 1000)
	@Size(max = 1000, message = "A url do vídeo deve ter no máximo 1000 caracteres.")
	private String urlVideo;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataCriacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getUrlImagem() {
		return urlImagem;
	}

	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}

	public String getUrlVideo() {
		return urlVideo;
	}

	public void setUrlVideo(String urlVideo) {
		this.urlVideo = urlVideo;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
}