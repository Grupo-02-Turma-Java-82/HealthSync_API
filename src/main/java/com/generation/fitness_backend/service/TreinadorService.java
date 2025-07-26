package com.generation.fitness_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.fitness_backend.enums.TipoUsuario;
import com.generation.fitness_backend.model.ListaAluno;
import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.repository.ListaAlunoRepository;
import com.generation.fitness_backend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TreinadorService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ListaAlunoRepository listaAlunoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario cadastrarAlunoParaTreinador(Usuario novoAluno, String emailTreinador) {

        if (usuarioRepository.findByEmail(novoAluno.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O email informado para o aluno já está em uso.");

        }
        Usuario treinador = usuarioRepository.findByEmail(emailTreinador)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Treinador não encontrado para vincular o aluno."));

        novoAluno.setTipoUsuario(TipoUsuario.ALUNO);
        novoAluno.setSenha(passwordEncoder.encode(novoAluno.getSenha()));
        novoAluno.setAtivo(true);

        Usuario alunoSalvo = usuarioRepository.save(novoAluno);

        ListaAluno vinculo = new ListaAluno();
        vinculo.setTreinador(treinador);
        vinculo.setAluno(alunoSalvo);
        listaAlunoRepository.save(vinculo);

        return alunoSalvo;
    }

    public List<ListaAluno> getAlunosDoTreinador(String emailTreinador) {
        Usuario treinador = usuarioRepository.findByEmail(emailTreinador)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Treinador não encontrado."));
        return listaAlunoRepository.findByTreinadorId(treinador.getId());

    }
}
