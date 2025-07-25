package com.generation.fitness_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.fitness_backend.enums.TipoUsuario;
import com.generation.fitness_backend.model.ListaAluno;
import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.repository.ListaAlunoRepository;
import com.generation.fitness_backend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class TreinadorService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private ListaAlunoRepository listaAlunoRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Transactional
  public Optional<Usuario> cadastrarAlunoParaTreinador(Usuario novoAluno, Usuario treinador) {

    if (usuarioRepository.findByEmail(novoAluno.getEmail()).isPresent()) {
      return Optional.empty();
    }

    novoAluno.setTipoUsuario(TipoUsuario.ALUNO);
    novoAluno.setSenha(passwordEncoder.encode(novoAluno.getSenha()));

    Usuario alunoSalvo = usuarioRepository.save(novoAluno);

    ListaAluno vinculo = new ListaAluno();
    vinculo.setTreinador(treinador);
    vinculo.setAluno(alunoSalvo);
    listaAlunoRepository.save(vinculo);

    return Optional.of(alunoSalvo);

  }

}
