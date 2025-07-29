package com.generation.fitness_backend.service;

import com.generation.fitness_backend.model.Treinos;
import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.enums.TipoUsuario;
import com.generation.fitness_backend.repository.TreinosRepository;
import com.generation.fitness_backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para TreinosService")
class TreinosServiceTest {

    @Mock
    private TreinosRepository treinosRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TreinosService treinosService;

    private Usuario usuario;
    private Treinos treino;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNomeCompleto("Bruno Teste");
        usuario.setEmail("bruno.teste@email.com");
        usuario.setSenha("Senhazipzipzip123");
        usuario.setTipoUsuario(TipoUsuario.ALUNO);
        usuario.setDataNascimento(LocalDate.of(2000, 1, 1));
        usuario.setGenero("Masculino");
        usuario.setAlturaCm(170);
        usuario.setPesoKg(new BigDecimal("70.0"));
        usuario.setObjetivoPrincipal("Ganhar massa");

        usuario.setTempoTotalExerciciosMinutos(0L);
        usuario.setTreinosRealizadosNaSemana(0);
        usuario.setSequenciaAtualDias(0);
        usuario.setMelhorSequenciaDias(0);
        usuario.setDataUltimoTreinoConcluido(null);

        treino = new Treinos();
        treino.setId(10L);
        treino.setNome("Treino de Teste");
        treino.setDescricao("Descrição do treino de teste");
        treino.setConcluido(false);
        treino.setTempoMinutos(30);
        treino.setUsuario(usuario);
    }

    @Test
    @DisplayName("Deve marcar treino como concluído e atualizar métricas do usuário corretamente")
    void testCompleteWorkout_marksAsConcluidoAndUpdatesMetrics() {
        when(treinosRepository.findById(anyLong())).thenReturn(Optional.of(treino));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(treinosRepository.save(any(Treinos.class))).thenReturn(treino);

        treinosService.completeWorkout(treino.getId());

        assertTrue(treino.isConcluido(), "O treino deve ser marcado como concluído.");
        assertNotNull(treino.getDataUltimaConclusao(), "A data de última conclusão deve ser setada.");
        assertEquals(30L, usuario.getTempoTotalExerciciosMinutos(), "O tempo total de exercícios do usuário deve ser 30.");
        assertEquals(1, usuario.getTreinosRealizadosNaSemana(), "Os treinos realizados na semana devem ser 1.");
        assertEquals(1, usuario.getSequenciaAtualDias(), "A sequência atual de dias deve ser 1.");
        assertEquals(1, usuario.getMelhorSequenciaDias(), "A melhor sequência de dias deve ser 1.");
        assertEquals(LocalDate.now(), usuario.getDataUltimoTreinoConcluido(), "A data do último treino concluído deve ser hoje.");

        verify(treinosRepository, times(1)).save(treino);
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção se o treino não for encontrado ao tentar concluir")
    void testCompleteWorkout_throwsExceptionWhenTreinoNotFound() {
        when(treinosRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            treinosService.completeWorkout(999L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode(), "O status deve ser NOT_FOUND.");
        assertTrue(exception.getReason().contains("Treino não encontrado"), "A mensagem deve indicar treino não encontrado.");

        verify(treinosRepository, never()).save(any(Treinos.class));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve desmarcar treino como concluído e reverter a contagem semanal de treinos")
    void testCompleteWorkout_unmarksAsConcluidoAndRevertsWeeklyCount() {
        treino.setConcluido(true);
        treino.setDataUltimaConclusao(LocalDateTime.of(2025, 7, 25, 10, 0));

        usuario.setTempoTotalExerciciosMinutos(500L);
        usuario.setTreinosRealizadosNaSemana(5);
        usuario.setSequenciaAtualDias(3);
        usuario.setMelhorSequenciaDias(5);
        usuario.setDataUltimoTreinoConcluido(LocalDate.of(2025, 7, 25));

        when(treinosRepository.findById(anyLong())).thenReturn(Optional.of(treino));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(treinosRepository.save(any(Treinos.class))).thenReturn(treino);

        treinosService.completeWorkout(treino.getId());

        assertFalse(treino.isConcluido(), "O treino deve ser desmarcado como concluído.");
        assertNull(treino.getDataUltimaConclusao(), "A data de última conclusão deve ser nula.");
        assertEquals(500L, usuario.getTempoTotalExerciciosMinutos(), "O tempo total de exercícios NÃO deve ser alterado ao desmarcar.");
        assertEquals(4, usuario.getTreinosRealizadosNaSemana(), "Os treinos realizados na semana devem ser 4 (decrementado em 1).");
        assertEquals(3, usuario.getSequenciaAtualDias(), "A sequência atual de dias NÃO deve ser alterada ao desmarcar.");
        assertEquals(LocalDate.of(2025, 7, 25), usuario.getDataUltimoTreinoConcluido(), "A data do último treino concluído NÃO deve ser alterada ao desmarcar.");

        verify(treinosRepository, times(1)).save(treino);
        verify(usuarioRepository, times(1)).save(usuario);
    }
}