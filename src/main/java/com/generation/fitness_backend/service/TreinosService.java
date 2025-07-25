package com.generation.fitness_backend.service;

import com.generation.fitness_backend.model.Treinos;
import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.repository.TreinosRepository;
import com.generation.fitness_backend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TreinosService {

    @Autowired
    private TreinosRepository treinosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Treinos> findAll() {
        return treinosRepository.findAll();
    }

    public Optional<Treinos> findById(Long id) {
        return treinosRepository.findById(id);
    }

    public List<Treinos> findByNome(String nome) {
        return treinosRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Treinos createTreino(Treinos treino) {
        if (treino.getUsuario() == null || treino.getUsuario().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID do usuário é obrigatório para criar um treino!");
        }

        Optional<Usuario> usuarioExistente = usuarioRepository.findById(treino.getUsuario().getId());
        if (usuarioExistente.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado para associar ao treino!");
        }

        treino.setUsuario(usuarioExistente.get());

        return treinosRepository.save(treino);
    }

    public Optional<Treinos> updateTreino(Treinos treino) {
        if (treino.getId() == null || !treinosRepository.existsById(treino.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Treino não encontrado para atualização!");
        }

        if (treino.getUsuario() != null && treino.getUsuario().getId() != null) {
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(treino.getUsuario().getId());
            if (usuarioExistente.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Novo usuário para associação do treino não encontrado!");
            }
            treino.setUsuario(usuarioExistente.get());
        } else {
            treino.setUsuario(treinosRepository.findById(treino.getId()).get().getUsuario());
        }

        return Optional.of(treinosRepository.save(treino));
    }

    public void deleteTreino(Long id) {
        if (!treinosRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Treino não encontrado para exclusão!");
        }
        treinosRepository.deleteById(id);
    }

    @Transactional
    public void completeWorkout(Long id) {
        Treinos treino = treinosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Treino não encontrado!"));

        treino.setConcluido(!treino.isConcluido());

        treinosRepository.save(treino);
    }
}