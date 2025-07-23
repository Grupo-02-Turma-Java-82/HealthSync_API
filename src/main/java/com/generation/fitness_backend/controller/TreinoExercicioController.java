package com.generation.fitness_backend.controller;

import com.generation.fitness_backend.model.TreinoExercicio;
import com.generation.fitness_backend.service.TreinoExercicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treinoexercicios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Treino Exercício", description = "Gerencia a associação entre treinos e exercícios")
@SecurityRequirement(name = "jwt_auth")
public class TreinoExercicioController {

    @Autowired
    private TreinoExercicioService treinoExercicioService;

    @Operation(summary = "Lista todas as associações de Treino-Exercício", description = "Retorna uma lista completa de todas as associações de treinos e exercícios.")
    @ApiResponse(responseCode = "200", description = "Lista de associações obtida com sucesso.")
    @GetMapping
    public ResponseEntity<List<TreinoExercicio>> getAll() {
        return ResponseEntity.ok(treinoExercicioService.findAll());
    }

    @Operation(summary = "Busca associação de Treino-Exercício por ID", description = "Retorna uma associação específica com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associação encontrada.", content = @Content(schema = @Schema(implementation = TreinoExercicio.class))),
            @ApiResponse(responseCode = "404", description = "Associação não encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TreinoExercicio> getById(@PathVariable Long id) {
        return treinoExercicioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Busca associações por ID do Treino", description = "Retorna todas as associações de exercícios para um treino específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de associações por treino obtida com sucesso.", content = @Content(schema = @Schema(implementation = TreinoExercicio.class, type = "array"))),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado.")
    })
    @GetMapping("/treino/{treinoId}")
    public ResponseEntity<List<TreinoExercicio>> getByTreinoId(@PathVariable Long treinoId) {
        return ResponseEntity.ok(treinoExercicioService.findByTreinoId(treinoId));
    }

    @Operation(summary = "Busca associações por ID do Exercício", description = "Retorna todas as associações de treinos para um exercício específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de associações por exercício obtida com sucesso.", content = @Content(schema = @Schema(implementation = TreinoExercicio.class, type = "array"))),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado.")
    })
    @GetMapping("/exercicio/{exercicioId}")
    public ResponseEntity<List<TreinoExercicio>> getByExercicioId(@PathVariable Long exercicioId) {
        return ResponseEntity.ok(treinoExercicioService.findByExercicioId(exercicioId));
    }

    @Operation(summary = "Cria uma nova associação de Treino-Exercício", description = "Associa um exercício a um treino existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Associação criada com sucesso.", content = @Content(schema = @Schema(implementation = TreinoExercicio.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: IDs de treino/exercício ausentes)."),
            @ApiResponse(responseCode = "404", description = "Treino ou Exercício não encontrados.")
    })
    @PostMapping
    public ResponseEntity<TreinoExercicio> post(@Valid @RequestBody TreinoExercicio treinoExercicio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(treinoExercicioService.createTreinoExercicio(treinoExercicio));
    }

    @Operation(summary = "Atualiza uma associação de Treino-Exercício existente", description = "Atualiza uma associação de treino-exercício. O ID da associação é obrigatório.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associação atualizada com sucesso.", content = @Content(schema = @Schema(implementation = TreinoExercicio.class))),
            @ApiResponse(responseCode = "400", description = "ID da associação não fornecido ou inválido."),
            @ApiResponse(responseCode = "404", description = "Associação, Treino ou Exercício não encontrados.")
    })
    @PutMapping
    public ResponseEntity<TreinoExercicio> put(@Valid @RequestBody TreinoExercicio treinoExercicio) {
        return treinoExercicioService.updateTreinoExercicio(treinoExercicio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Deleta uma associação de Treino-Exercício", description = "Remove uma associação de treino-exercício com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Associação deletada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Associação não encontrada para exclusão.")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        treinoExercicioService.deleteTreinoExercicio(id);
    }
}