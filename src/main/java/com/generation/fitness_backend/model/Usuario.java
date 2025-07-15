package com.generation.fitness_backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // de integer para long
	private Long id;

	@NotBlank(message = "O atributo Nome Completo é obrigatório!")
	@Column(nullable = false, length = 255)
	private String nomeCompleto;

	@NotNull(message = "O atributo Email é obrigatório!")
	@Email(message = "O atributo Email deve ser válido!")
	@Column(length = 100, nullable = false, unique = true)
	private String email;

	@NotBlank(message = "O atributo Senha é obrigatório!")
	@Size(min = 8, message = "A Senha deve ter no mínimo 8 caracteres")
	@Column(length = 100, nullable = false)
	private String senha;

	@NotNull(message = "A data de nascimento é obrigatória")
	@Column(length = 100, nullable = false)
	private LocalDate dataNascimento;

	@Column(length = 100, nullable = false)
	private String genero;

	@NotNull(message = "A altura é obrigatória")
	@Column(length = 100, nullable = false)
	private Integer alturaCm;

	@NotNull(message = "O peso é obrigatório")
	@Column(length = 100, nullable = false)
	private BigDecimal pesoKg;
	
    @Transient // Indica que este campo não será persistido no banco de dados
    private Double imc;

	@Column(length = 255, nullable = false)
	private String objetivoPrincipal;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message = "O tipo do usuario é obrigatorio")
	@Column(name = "tipo", nullable = false, length = 50)
	private TipoUsuario tipoUsuario;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataCadastro;
	
	@Column(nullable = true)
	private LocalDateTime dataRemocao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Integer getAlturaCm() {
		return alturaCm;
	}

	public void setAlturaCm(Integer alturaCm) {
		this.alturaCm = alturaCm;
	}

	public BigDecimal getPesoKg() {
		return pesoKg;
	}

	public void setPesoKg(BigDecimal pesoKg) {
		this.pesoKg = pesoKg;
	}
	
    // Getter para o IMC (calculado dinamicamente)
    public Double getImc() {
        if (this.pesoKg != null && this.alturaCm != null && this.alturaCm > 0) {
        	
        	Double alturaCalc = (double) (this.alturaCm * this.alturaCm);
            return (this.pesoKg.doubleValue() / (alturaCalc)) * 10000;
        }

        return null;
    }

    // O setter para o IMC não é necessário, pois ele é calculado
    // Mas se você quiser ter um (por exemplo, para DTOs que o aceitem), pode adicionar
    public void setImc(Double imc) {
        this.imc = imc;
    }

	public String getObjetivoPrincipal() {
		return objetivoPrincipal;
	}

	public void setObjetivoPrincipal(String objetivoPrincipal) {
		this.objetivoPrincipal = objetivoPrincipal;
	}
	
	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public LocalDateTime getDataRemocao() {
		return dataRemocao;
	}

	public void setDataRemocao(LocalDateTime dataRemocao) {
		this.dataRemocao = dataRemocao;
	}
}
