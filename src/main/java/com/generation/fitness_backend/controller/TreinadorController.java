package com.generation.fitness_backend.controller;

import com.generation.fitness_backend.model.ListaAluno;
import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.repository.ListaAlunoRepository;
import com.generation.fitness_backend.repository.UsuarioRepository;
import com.generation.fitness_backend.service.TreinadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/treinadores")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Treinador", description = "Endpoints específicos para ações realizadas por treinadores")
@SecurityRequirement(name = "jwt_auth")
@PreAuthorize("hasRole('TREINADOR')")
public class TreinadorController {

  @Autowired
  private TreinadorService treinadorService;

  @Operation(summary = "Treinador cadastra um novo aluno", description = "Cria um novo usuário do tipo ALUNO e o vincula automaticamente ao treinador que está logado.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Aluno cadastrado e vinculado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Requisição inválida - O email do aluno já existe."),
      @ApiResponse(responseCode = "403", description = "Acesso Proibido")
  })

  @PostMapping("/alunos")
  public ResponseEntity<Usuario> cadastrarAluno(@Valid @RequestBody Usuario novoAluno, Authentication authentication) {
    String emailTreinador = authentication.getName();

    return ResponseEntity.status(HttpStatus.CREATED).body(treinadorService.cadastrarAlunoParaTreinador(novoAluno, emailTreinador));
  }

  @Operation(summary = "Lista os alunos do treinador logado", description = "Retorna a lista de todos os alunos vinculados ao treinador que está fazendo a requisição.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de vínculos retornada com sucesso.", content = @Content(schema = @Schema(implementation = ListaAluno.class, type = "array"))),
      @ApiResponse(responseCode = "403", description = "Acesso Proibido"),
      @ApiResponse(responseCode = "404", description = "Treinador não encontrado")
  })
  @GetMapping("/meus-alunos")
  public ResponseEntity<List<ListaAluno>> getMeusAlunos(Authentication authentication) {

    String emailTreinador = authentication.getName();
    return ResponseEntity.ok(treinadorService.getAlunosDoTreinador(emailTreinador));
  }
}