package com.generation.fitness_backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_pesos")
public class Pesos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "O peso é obrigatório")
	@Column(length = 100, nullable = false)
	private BigDecimal pesoKg;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataRegistro;

	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	@JsonIgnoreProperties({ "pesos", "dataCadastro", "dataDesativacao" })
	private Usuario usuario;

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

	public BigDecimal getPesoKg() {
		return pesoKg;
	}

	public void setPesoKg(BigDecimal pesoKg) {
		this.pesoKg = pesoKg;
	}

	public LocalDateTime getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(LocalDateTime dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

}
