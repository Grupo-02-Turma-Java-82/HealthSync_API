package com.generation.fitness_backend.service;

import com.generation.fitness_backend.model.Treinos;
import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.repository.TreinosRepository;
import com.generation.fitness_backend.repository.UsuarioRepository;
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

    // Método para listar todos os treinos
    public List<Treinos> findAll() {
        return treinosRepository.findAll();
    }

    // Método para buscar um treino por ID
    public Optional<Treinos> findById(Long id) {
        return treinosRepository.findById(id);
    }

    // Método para buscar treinos por nome
    public List<Treinos> findByNome(String nome) {
        return treinosRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Método para criar um novo treino
    public Treinos createTreino(Treinos treino) {
        // Valida se o usuário associado ao treino existe
        if (treino.getUsuario() == null || treino.getUsuario().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do usuário é obrigatório para criar um treino!");
        }

        Optional<Usuario> usuarioExistente = usuarioRepository.findById(treino.getUsuario().getId());
        if (usuarioExistente.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado para associar ao treino!");
        }

        // Associa o objeto Usuario completo ao Treino antes de salvar
        treino.setUsuario(usuarioExistente.get());

        return treinosRepository.save(treino);
    }

    // Método para atualizar um treino existente
    public Optional<Treinos> updateTreino(Treinos treino) {
        // Verifica se o treino a ser atualizado existe
        if (treino.getId() == null || !treinosRepository.existsById(treino.getId())) {
            // Lança exceção se o treino não for encontrado, encapsulando no Optional.empty()
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Treino não encontrado para atualização!");
        }

        // Valida se o usuário associado (se for alterado) existe
        if (treino.getUsuario() != null && treino.getUsuario().getId() != null) {
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(treino.getUsuario().getId());
            if (usuarioExistente.isEmpty()) {
                // Lança exceção se o novo usuário para associação não for encontrado
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Novo usuário para associação do treino não encontrado!");
            }
            treino.setUsuario(usuarioExistente.get()); // Associa o objeto Usuario completo
        } else {
            // Se um novo usuário não for fornecido, mantém o usuário existente do treino
            treino.setUsuario(treinosRepository.findById(treino.getId()).get().getUsuario());
        }

        return Optional.of(treinosRepository.save(treino));
    }

    // Método para deletar um treino
    public void deleteTreino(Long id) {
        if (!treinosRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Treino não encontrado para exclusão!");
        }
        treinosRepository.deleteById(id);
    }
}