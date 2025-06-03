package com.generation.fitness_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Past;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotBlank(message = "O atributo Nome Completo é obrigatório!")
    @Column(nullable = false, length = 255)
    private String nome_completo;
    
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
    private int altura_cm;
    
    @NotNull(message = "O peso é obrigatório")
    @Column(length = 100, nullable = false)
    private BigDecimal peso_kg;
    
    @Column(length = 255, nullable = false)
    private String objetivo_principal;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime data_cadastro;
     
    
}
