package com.generation.fitness_backend.repository;

import com.generation.fitness_backend.model.TreinoExercicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TreinoExercicioRepository extends JpaRepository<TreinoExercicio, Long> {

    List<TreinoExercicio> findByTreinoId(Long treinoId);

    List<TreinoExercicio> findByExercicioId(Long exercicioId);

    Optional<TreinoExercicio> findByTreinoIdAndExercicioId(Long treinoId, Long exercicioId);

}
