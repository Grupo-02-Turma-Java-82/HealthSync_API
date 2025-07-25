package com.generation.fitness_backend.controller;

import com.generation.fitness_backend.model.Treinos;
import com.generation.fitness_backend.service.TreinosService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treinos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Treinos", description = "Gerencia os treinos criados pelos usuários")
@SecurityRequirement(name = "jwt_auth")
public class TreinosController {

    @Autowired
    private TreinosService treinosService;

    @GetMapping
    public ResponseEntity<List<Treinos>> getAll() {
        return ResponseEntity.ok(treinosService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Treinos> getById(@PathVariable Long id) {
        return treinosService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Treinos>> getByNome(@PathVariable String nome) {
        return ResponseEntity.ok(treinosService.findByNome(nome));
    }

    @PostMapping
    public ResponseEntity<Treinos> post(@Valid @RequestBody Treinos treino) {
        return ResponseEntity.status(HttpStatus.CREATED).body(treinosService.createTreino(treino));
    }

    @PutMapping
    public ResponseEntity<Treinos> put(@Valid @RequestBody Treinos treino) {
        return ResponseEntity.status(HttpStatus.OK).body(treinosService.updateTreino(treino));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        treinosService.deleteTreino(id);
    }

    @PatchMapping("/{id}/concluido")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarConcluido(@PathVariable Long id) {
        treinosService.completeWorkout(id);
    }
}