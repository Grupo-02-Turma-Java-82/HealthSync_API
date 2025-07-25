package com.generation.fitness_backend.service;

import com.generation.fitness_backend.model.Exercicios;
import com.generation.fitness_backend.model.TreinoExercicio;
import com.generation.fitness_backend.model.Treinos;
import com.generation.fitness_backend.repository.ExerciciosRepository;
import com.generation.fitness_backend.repository.TreinoExercicioRepository;
import com.generation.fitness_backend.repository.TreinosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TreinoExercicioService {

    @Autowired
    private TreinoExercicioRepository treinoExercicioRepository;

    @Autowired
    private TreinosRepository treinosRepository;

    @Autowired
    private ExerciciosRepository exerciciosRepository;

    public List<TreinoExercicio> findAll() {
        return treinoExercicioRepository.findAll();
    }

    public Optional<TreinoExercicio> findById(Long id) {
        return treinoExercicioRepository.findById(id);
    }

    public TreinoExercicio createTreinoExercicio(TreinoExercicio treinoExercicio) {

        if (treinoExercicio.getTreino() == null || treinoExercicio.getTreino().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do Treino é obrigatório!");
        }

        if (treinoExercicio.getExercicio() == null || treinoExercicio.getExercicio().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do Exercício é obrigatório!");
        }

        Long treinoId = treinoExercicio.getTreino().getId();
        Long exercicioId = treinoExercicio.getExercicio().getId();

        Optional<Treinos> existingTreino = treinosRepository.findById(treinoId);
        Optional<Exercicios> existingExercicio = exerciciosRepository.findById(exercicioId);

        if (existingTreino.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Treino não encontrado!");
        }

        if (existingExercicio.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercício não encontrado!");
        }

        if (treinoExercicioRepository.findByTreinoIdAndExercicioId(treinoId, exercicioId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta associação de Treino e Exercício já existe!");
        }

        treinoExercicio.setTreino(existingTreino.get());
        treinoExercicio.setExercicio(existingExercicio.get());

        return treinoExercicioRepository.save(treinoExercicio);
    }

    public Optional<TreinoExercicio> updateTreinoExercicio(TreinoExercicio treinoExercicio) {
        if (treinoExercicio.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID da associação de treino e exercício é obrigatório para atualização!");
        }

        return treinoExercicioRepository.findById(treinoExercicio.getId())
                .map(existingTreinoExercicio -> {

                    if (treinoExercicio.getTreino() != null && treinoExercicio.getTreino().getId() != null) {
                        treinosRepository.findById(treinoExercicio.getTreino().getId())
                                .ifPresentOrElse(existingTreinoExercicio::setTreino,
                                        () -> {
                                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Novo Treino para atualização não encontrado com ID: " + treinoExercicio.getTreino().getId());
                                        });
                    }

                    if (treinoExercicio.getExercicio() != null && treinoExercicio.getExercicio().getId() != null) {
                        exerciciosRepository.findById(treinoExercicio.getExercicio().getId())
                                .ifPresentOrElse(existingTreinoExercicio::setExercicio,
                                        () -> {
                                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Novo Exercício para atualização não encontrado com ID: " + treinoExercicio.getExercicio().getId());
                                        });
                    }

                    return Optional.of(treinoExercicioRepository.save(existingTreinoExercicio));
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Associação de treino e exercício não encontrada para o ID: " + treinoExercicio.getId()));

            }

    public void deleteTreinoExercicio(Long id) {
        if (!treinoExercicioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Associação de treino e exercício não encontrada!");
        }
        treinoExercicioRepository.deleteById(id);
    }

    public List<TreinoExercicio> findByTreinoId(Long treinoId) {
        if (treinosRepository.findById(treinoId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Treino com ID " + treinoId + " não encontrado.");
        }
        return treinoExercicioRepository.findByTreinoId(treinoId);
    }

    public List<TreinoExercicio> findByExercicioId(Long exercicioId) {
        if (exerciciosRepository.findById(exercicioId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Exercício com ID " + exercicioId + " não encontrado.");
        }
        return treinoExercicioRepository.findByExercicioId(exercicioId);
    }
}