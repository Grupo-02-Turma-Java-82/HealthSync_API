package com.generation.fitness_backend.repository;

import com.generation.fitness_backend.model.Treinos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreinosRepository extends JpaRepository<Treinos, Long> {

    List<Treinos> findByNomeContainingIgnoreCase(String nome);

}
