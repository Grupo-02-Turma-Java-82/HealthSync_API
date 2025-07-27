package com.generation.fitness_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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

    @Column(nullable = false)
    private boolean concluido = false;

    @Column(nullable = true)
    private LocalDateTime dataUltimaConclusao;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @NotNull(message = "O tempo estimado do treino é obrigatório!")
    @Min(value = 1, message = "O tempo estimado deve ser no mínimo 1 minuto.")
    @Column(nullable = false)
    private Integer tempoMinutos;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnoreProperties({"treinos", "dataCadastro", "dataDesativacao"})
    private Usuario usuario;

    @OneToMany(mappedBy = "treino", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("treino")
    private List<TreinoExercicio> treinoExercicios;

    @OneToMany(mappedBy = "treino", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("treino")
    private List<TreinoRegistro> treinoRegistros;

    public Treinos() {
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

    public boolean isConcluido() {
        return this.concluido;
    }

    public boolean getConcluido() {
        return this.concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public List<TreinoRegistro> getTreinoRegistros() {
        return treinoRegistros;
    }

    public void setTreinoRegistros(List<TreinoRegistro> treinoRegistros) {
        this.treinoRegistros = treinoRegistros;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataUltimaConclusao() {
        return dataUltimaConclusao;
    }

    public void setDataUltimaConclusao(LocalDateTime dataUltimaConclusao) {
        this.dataUltimaConclusao = dataUltimaConclusao;
    }

    public Integer getTempoMinutos() {
        return tempoMinutos;
    }

    public void setTempoMinutos(Integer tempoMinutos) {
        this.tempoMinutos = tempoMinutos;
    }
}

