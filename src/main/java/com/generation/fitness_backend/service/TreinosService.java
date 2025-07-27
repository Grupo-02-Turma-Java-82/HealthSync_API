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

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Transactional
    public Treinos createTreino(Treinos treino) {

        if (treino.getUsuario() == null || treino.getUsuario().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID do usuário é obrigatório para criar um treino!");
        }

        Usuario usuarioExistente = usuarioRepository.findById(treino.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado para associar ao treino!"));

        treino.setUsuario(usuarioExistente);
        treino.setConcluido(false);
        treino.setDataUltimaConclusao(null);

        return treinosRepository.save(treino);
    }

    @Transactional
    public Treinos updateTreino(Treinos treino) {

        Treinos treinoExistente = treinosRepository.findById(treino.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Treino não encontrado para atualização!"));

        if (treino.getUsuario() != null && treino.getUsuario().getId() != null) {
            Usuario novoUsuario = usuarioRepository.findById(treino.getUsuario().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Novo usuário para associação do treino não encontrado com ID: " + treino.getUsuario().getId()));
            treinoExistente.setUsuario(novoUsuario);
        }

        treinoExistente.setNome(treino.getNome());
        treinoExistente.setDescricao(treino.getDescricao());
        treinoExistente.setTempoMinutos(treino.getTempoMinutos());

        return treinosRepository.save(treinoExistente);
    }

    @Transactional
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

        Usuario usuario = treino.getUsuario();

        boolean novoStatusConcluido = !treino.isConcluido();
        treino.setConcluido(novoStatusConcluido);

        if (novoStatusConcluido) {
            treino.setDataUltimaConclusao(LocalDateTime.now());

            if (treino.getTempoMinutos() != null) {
                usuario.setTempoTotalExerciciosMinutos(usuario.getTempoTotalExerciciosMinutos() + treino.getTempoMinutos());
            }

            usuario.setTreinosRealizadosNaSemana(usuario.getTreinosRealizadosNaSemana() + 1);

            LocalDate hoje = LocalDate.now();
            if (usuario.getDataUltimoTreinoConcluido() == null) {
                usuario.setSequenciaAtualDias(1);
            } else if (usuario.getDataUltimoTreinoConcluido().isEqual(hoje.minusDays(1))) {
                usuario.setSequenciaAtualDias(usuario.getSequenciaAtualDias() + 1);
            } else if (usuario.getDataUltimoTreinoConcluido().isBefore(hoje.minusDays(1))) {
                usuario.setSequenciaAtualDias(1);
            }

            if (usuario.getSequenciaAtualDias() > usuario.getMelhorSequenciaDias()) {
                usuario.setMelhorSequenciaDias(usuario.getSequenciaAtualDias());
            }

            usuario.setDataUltimoTreinoConcluido(hoje);

        } else {
            treino.setDataUltimaConclusao(null);

            usuario.setTreinosRealizadosNaSemana(usuario.getTreinosRealizadosNaSemana() - 1);
            if (usuario.getTreinosRealizadosNaSemana() < 0) {
                usuario.setTreinosRealizadosNaSemana(0);
            }
        }
        treinosRepository.save(treino);
        usuarioRepository.save(usuario);
    }
}