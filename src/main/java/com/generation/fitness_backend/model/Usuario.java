package com.generation.fitness_backend.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.generation.fitness_backend.enums.TipoUsuario;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O atributo Nome Completo é obrigatório!")
    @Column(nullable = false, length = 255)
    private String nomeCompleto;

    @Column(length = 1000, nullable = true)
    @Size(max = 1000, message = "A url da imagem deve ter no máximo 1000 caracteres.")
    private String urlImagem;

    @NotBlank(message = "O atributo Email é obrigatório!")
    @Email(message = "O atributo Email deve ser válido!")
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "O atributo Senha é obrigatório!")
    @Size(min = 8, message = "A Senha deve ter no mínimo 8 caracteres")
    @Column(length = 100, nullable = false)
    private String senha;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Column(nullable = false)
    private LocalDate dataNascimento;

    @NotBlank(message = "O atributo Gênero é obrigatório!")
    @Column(length = 100, nullable = false)
    private String genero;

    @Column(nullable = true)
    private Integer alturaCm;

    @Column(nullable = true, precision = 10, scale = 2)
    private BigDecimal pesoKg;

    @Column(length = 255, nullable = true)
    @Size(max = 255, message = "O objetivo principal deve ter no máximo 255 caracteres.")
    private String objetivoPrincipal;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 50)
    private TipoUsuario tipoUsuario;

    @Column(nullable = false)
    private boolean ativo = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(nullable = true)
    private LocalDateTime dataDesativacao;

    @Transient
    private BigDecimal imc;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("usuario")
    @JsonIgnore
    private List<AtividadeRegistro> atividadesRegistro;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("usuario")
    @JsonIgnore
    private List<Pesos> pesos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("usuario")
    @JsonIgnore
    private List<Treinos> treinos;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("aluno")
    @JsonIgnore
    private List<ListaAluno> vinculosComoAluno;

    @OneToMany(mappedBy = "treinador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("treinador")
    @JsonIgnore
    private List<ListaAluno> vinculosComoTreinador;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("usuario")
    @JsonIgnore
    private List<ExercicioRegistro> exercicioRegistros;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("usuario")
    @JsonIgnore
    private List<TreinoRegistro> treinoRegistros;

    @PostLoad
    @PrePersist
    @PreUpdate
    private void calculateImc() {

        if (this.pesoKg != null && this.alturaCm != null && this.alturaCm > 0) {
            BigDecimal alturaMetros = BigDecimal.valueOf(this.alturaCm)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            if (alturaMetros.compareTo(BigDecimal.ZERO) > 0) {
                this.imc = this.pesoKg.divide(alturaMetros.multiply(alturaMetros), 2, RoundingMode.HALF_UP);
            } else {
                this.imc = null;
            }
        } else {
            this.imc = null;
        }
    }

    public Usuario() {
        this.ativo = true;
    }

    public Usuario(Long id, String nomeCompleto, String urlImagem, String email, String senha, LocalDate dataNascimento,
            String genero, Integer alturaCm, BigDecimal pesoKg, String objetivoPrincipal, TipoUsuario tipoUsuario,
            boolean ativo) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.urlImagem = urlImagem;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.alturaCm = alturaCm;
        this.pesoKg = pesoKg;
        this.objetivoPrincipal = objetivoPrincipal;
        this.tipoUsuario = tipoUsuario;
        this.ativo = ativo;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "O atributo Nome Completo é obrigatório!") String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(@NotBlank(message = "O atributo Nome Completo é obrigatório!") String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public @Size(max = 1000, message = "A url da imagem deve ter no máximo 1000 caracteres.") String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(
            @Size(max = 1000, message = "A url da imagem deve ter no máximo 1000 caracteres.") String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public @NotBlank(message = "O atributo Email é obrigatório!") @Email(message = "O atributo Email deve ser válido!") String getEmail() {
        return email;
    }

    public void setEmail(
            @NotBlank(message = "O atributo Email é obrigatório!") @Email(message = "O atributo Email deve ser válido!") String email) {
        this.email = email;
    }

    public @NotBlank(message = "O atributo Senha é obrigatório!") @Size(min = 8, message = "A Senha deve ter no mínimo 8 caracteres") String getSenha() {
        return senha;
    }

    public void setSenha(
            @NotBlank(message = "O atributo Senha é obrigatório!") @Size(min = 8, message = "A Senha deve ter no mínimo 8 caracteres") String senha) {
        this.senha = senha;
    }

    public @NotNull(message = "A data de nascimento é obrigatória") LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(@NotNull(message = "A data de nascimento é obrigatória") LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public @NotBlank(message = "O atributo Gênero é obrigatório!") String getGenero() {
        return genero;
    }

    public void setGenero(@NotBlank(message = "O atributo Gênero é obrigatório!") String genero) {
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

    public @Size(max = 255, message = "O objetivo principal deve ter no máximo 255 caracteres.") String getObjetivoPrincipal() {
        return objetivoPrincipal;
    }

    public void setObjetivoPrincipal(
            @Size(max = 255, message = "O objetivo principal deve ter no máximo 255 caracteres.") String objetivoPrincipal) {
        this.objetivoPrincipal = objetivoPrincipal;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataDesativacao() {
        return dataDesativacao;
    }

    public void setDataDesativacao(LocalDateTime dataDesativacao) {
        this.dataDesativacao = dataDesativacao;
    }

    public BigDecimal getImc() {
        return imc;
    }

    public void setImc(BigDecimal imc) {
        this.imc = imc;
    }

    public List<AtividadeRegistro> getAtividadesRegistro() {
        return atividadesRegistro;
    }

    public void setAtividadesRegistro(List<AtividadeRegistro> atividadesRegistro) {
        this.atividadesRegistro = atividadesRegistro;
    }

    public List<Pesos> getPesos() {
        return pesos;
    }

    public void setPesos(List<Pesos> pesos) {
        this.pesos = pesos;
    }

    public List<Treinos> getTreinos() {
        return treinos;
    }

    public void setTreinos(List<Treinos> treinos) {
        this.treinos = treinos;
    }

    public List<ListaAluno> getVinculosComoAluno() {
        return vinculosComoAluno;
    }

    public void setVinculosComoAluno(List<ListaAluno> vinculosComoAluno) {
        this.vinculosComoAluno = vinculosComoAluno;
    }

    public List<ListaAluno> getVinculosComoTreinador() {
        return vinculosComoTreinador;
    }

    public void setVinculosComoTreinador(List<ListaAluno> vinculosComoTreinador) {
        this.vinculosComoTreinador = vinculosComoTreinador;
    }

    public List<ExercicioRegistro> getExercicioRegistros() {
        return exercicioRegistros;
    }

    public void setExercicioRegistros(List<ExercicioRegistro> exercicioRegistros) {
        this.exercicioRegistros = exercicioRegistros;
    }

    public List<TreinoRegistro> getTreinoRegistros() {
        return treinoRegistros;
    }

    public void setTreinoRegistros(List<TreinoRegistro> treinoRegistros) {
        this.treinoRegistros = treinoRegistros;
    }
}
