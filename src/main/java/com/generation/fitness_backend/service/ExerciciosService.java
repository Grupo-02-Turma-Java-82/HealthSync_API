package com.generation.fitness_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.fitness_backend.model.Exercicios;
import com.generation.fitness_backend.repository.ExerciciosRepository;

import jakarta.transaction.Transactional;

@Service
public class ExerciciosService {

    @Autowired
    private ExerciciosRepository exerciciosRepository;

    public List<Exercicios> findAll() {
        return exerciciosRepository.findAll();
    }

    public Optional<Exercicios> findById(Long id) {
        return exerciciosRepository.findById(id);
    }

    public List<Exercicios> findByNome(String nome) {
        return exerciciosRepository.findAllByNomeContainingIgnoreCase(nome);
    }

    @Transactional
    public Exercicios criar(Exercicios exercicio) {
        return exerciciosRepository.save(exercicio);
    }

    @Transactional
    public Exercicios atualizar(Exercicios exercicio) {

        Exercicios exercicioExistente = exerciciosRepository.findById(exercicio.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercício não encontrado para atualização!"));

        exercicioExistente.setNome(exercicio.getNome());
        exercicioExistente.setDescricaoDetalhada(exercicio.getDescricaoDetalhada());
        exercicioExistente.setNivelDificuldade(exercicio.getNivelDificuldade());
        exercicioExistente.setUrlVideoDemonstrativo(exercicio.getUrlVideoDemonstrativo());
        exercicioExistente.setEquipamentoNecessario(exercicio.getEquipamentoNecessario());
        exercicioExistente.setCategoria(exercicio.getCategoria());

        return exerciciosRepository.save(exercicioExistente);
    }

    @Transactional
    public void deleteById(Long id) {
        if (exerciciosRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercício não encontrado para exclusão!");
        }
        exerciciosRepository.deleteById(id);
    }
}