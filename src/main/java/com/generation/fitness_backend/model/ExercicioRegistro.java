package com.generation.fitness_backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_exercicio_registro")
public class ExercicioRegistro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
	@JoinColumn(name = "id_exercicio", nullable = false)
	@JsonIgnoreProperties("exercicioRegistros")
    private Exercicios exercicio;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnoreProperties({"exercicioRegistros", "pesos", "treinos", "atividadesRegistro", "listaAlunos"})
    private Usuario usuario;

    @NotNull(message = "A a duração dos exercicos é obrigatória")
    @Column(nullable = false)
    private Integer duracao;

    @NotNull(message = "A quantidade de repeticao é obrigatória")
    @Column(length = 100, nullable = false)
    private Integer repeticao;

    @NotNull(message = "A carga é obrigatória")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal carga;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    public ExercicioRegistro() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exercicios getExercicio() {
        return exercicio;
    }

    public void setExercicio(Exercicios exercicio) {
        this.exercicio = exercicio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public Integer getRepeticao() {
        return repeticao;
    }

    public void setRepeticao(Integer repeticao) {
        this.repeticao = repeticao;
    }

    public BigDecimal getCarga() {
        return carga;
    }

    public void setCarga(BigDecimal carga) {
        this.carga = carga;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

}
