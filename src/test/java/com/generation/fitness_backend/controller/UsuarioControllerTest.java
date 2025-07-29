package com.generation.fitness_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.enums.TipoUsuario;
import com.generation.fitness_backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes de Integração para UsuarioController")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve cadastrar um novo usuário com sucesso")
    void testCadastrarNovoUsuario() throws Exception {
        String usuarioJson = "{\"nomeCompleto\":\"Usuário Teste Integracao\","
                + "\"email\":\"integracao.teste@email.com\","
                + "\"senha\":\"SenhaSegura123!\","
                + "\"dataNascimento\":\"2000-01-01\","
                + "\"genero\":\"Não Binário\","
                + "\"alturaCm\":170,"
                + "\"pesoKg\":70.0,"
                + "\"objetivoPrincipal\":\"Ganhar massa\","
                + "\"tipoUsuario\":\"ALUNO\"}";

        mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("integracao.teste@email.com"));

        Optional<Usuario> usuarioSalvo = usuarioRepository.findByEmail("integracao.teste@email.com");
        assertTrue(usuarioSalvo.isPresent());
        assertTrue(passwordEncoder.matches("SenhaSegura123!", usuarioSalvo.get().getSenha()));
    }

    @Test
    @DisplayName("Não deve cadastrar usuário com email duplicado")
    void testNaoDeveCadastrarUsuarioComEmailDuplicado() throws Exception {
        String usuarioExistenteJson = "{\"nomeCompleto\":\"Usuário Existente\","
                + "\"email\":\"duplicado@email.com\","
                + "\"senha\":\"SenhaExistente!\","
                + "\"dataNascimento\":\"1990-05-10\","
                + "\"genero\":\"Masculino\","
                + "\"alturaCm\":180,"
                + "\"pesoKg\":80.0,"
                + "\"objetivoPrincipal\":\"Manter peso\","
                + "\"tipoUsuario\":\"ALUNO\"}";
        mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioExistenteJson))
                .andExpect(status().isCreated());

        String novoUsuarioComEmailDuplicadoJson = "{\"nomeCompleto\":\"Novo Usuário\","
                + "\"email\":\"duplicado@email.com\","
                + "\"senha\":\"NovaSenhaDuplicada!\","
                + "\"dataNascimento\":\"1995-08-20\","
                + "\"genero\":\"Feminino\","
                + "\"alturaCm\":165,"
                + "\"pesoKg\":60.0,"
                + "\"objetivoPrincipal\":\"Ganhar força\","
                + "\"tipoUsuario\":\"ALUNO\"}";

        mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novoUsuarioComEmailDuplicadoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Não deve cadastrar ALUNO sem altura, peso ou objetivo principal")
    void testNaoDeveCadastrarAlunoSemDadosObrigatorios() throws Exception {
        String alunoIncompletoJson = "{\"nomeCompleto\":\"Aluno Incompleto\","
                + "\"email\":\"aluno.incompleto@email.com\","
                + "\"senha\":\"SenhaAluno!\","
                + "\"dataNascimento\":\"2001-03-15\","
                + "\"genero\":\"Não Informado\","
                + "\"tipoUsuario\":\"ALUNO\"}";

        mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alunoIncompletoJson))
                .andExpect(status().isBadRequest());
    }

    private String obterTokenDeLogin(String email, String senha, TipoUsuario tipoUsuario) throws Exception {
        String usuarioCadastroJson = String.format(
                "{\"nomeCompleto\":\"Usuario Cadastrado para Login\"," +
                        "\"email\":\"%s\"," +
                        "\"senha\":\"%s\"," +
                        "\"dataNascimento\":\"1990-01-01\"," +
                        "\"genero\":\"Masculino\"," +
                        "\"alturaCm\":175," +
                        "\"pesoKg\":70.0," +
                        "\"objetivoPrincipal\":\"Manter\"," +
                        "\"tipoUsuario\":\"%s\"}",
                email, senha, tipoUsuario.name()
        );

        mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioCadastroJson))
                .andExpect(status().isCreated());

        String loginJson = String.format("{\"email\":\"%s\",\"senha\":\"%s\"}", email, senha);
        String response = mockMvc.perform(post("/usuarios/logar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    @DisplayName("Deve logar usuário com credenciais válidas e retornar token")
    void testLogarUsuarioComCredenciaisValidas() throws Exception {
        String token = obterTokenDeLogin("loginvalido@email.com", "SenhaValida123!", TipoUsuario.ALUNO);
        assertNotNull(token);
        assertTrue(token.startsWith("Bearer "));
    }

    @Test
    @DisplayName("Não deve logar usuário com senha inválida")
    void testNaoDeveLogarUsuarioComSenhaInvalida() throws Exception {
        String email = "senhainvalida@email.com";
        String senhaCorreta = "SenhaCorreta123!";
        String senhaInvalida = "SenhaErrada!";

        String usuarioCadastroJson = String.format(
                "{\"nomeCompleto\":\"Usuario Teste Senha Invalida Cad\"," +
                        "\"email\":\"%s\"," +
                        "\"senha\":\"%s\","
                        + "\"dataNascimento\":\"1990-01-01\","
                        + "\"genero\":\"Masculino\","
                        + "\"alturaCm\":170,"
                        + "\"pesoKg\":70.0,"
                        + "\"objetivoPrincipal\":\"Manter\","
                        + "\"tipoUsuario\":\"ALUNO\"}",
                email, senhaCorreta
        );
        mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioCadastroJson))
                .andExpect(status().isCreated());

        String loginJson = String.format("{\"email\":\"%s\",\"senha\":\"%s\"}", email, senhaInvalida);
        mockMvc.perform(post("/usuarios/logar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve buscar todos os usuários com token de TREINADOR válido")
    void testBuscarTodosUsuariosComTokenValido() throws Exception {
        String tokenTreinador = obterTokenDeLogin("treinador.busca@email.com", "SenhaTreinador123!", TipoUsuario.TREINADOR);

        String usuarioAlunoJson = "{\"nomeCompleto\":\"Usuario Aluno Teste\","
                + "\"email\":\"usuario.aluno@email.com\","
                + "\"senha\":\"SenhaAluno123!\","
                + "\"dataNascimento\":\"1995-02-02\","
                + "\"genero\":\"Feminino\","
                + "\"alturaCm\":160,"
                + "\"pesoKg\":55.0,"
                + "\"objetivoPrincipal\":\"Perder peso\","
                + "\"tipoUsuario\":\"ALUNO\"}";
        mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioAlunoJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/usuarios")
                        .header("Authorization", tokenTreinador))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Não deve buscar usuários sem token (Unauthorized)")
    void testNaoBuscarUsuariosSemToken() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso (com token válido)")
    void testBuscarUsuarioPorIdComSucesso() throws Exception {
        String tokenTreinador = obterTokenDeLogin("treinador.buscaid@email.com", "SenhaBuscaId123!", TipoUsuario.TREINADOR);

        String usuarioBuscadoJson = "{\"nomeCompleto\":\"Usuario Buscado Teste\","
                + "\"email\":\"buscado@email.com\","
                + "\"senha\":\"SenhaBuscado123!\","
                + "\"dataNascimento\":\"1998-04-20\","
                + "\"genero\":\"Feminino\","
                + "\"alturaCm\":165,"
                + "\"pesoKg\":60.0,"
                + "\"objetivoPrincipal\":\"Definicao\","
                + "\"tipoUsuario\":\"ALUNO\"}";
        String responseCadastro = mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioBuscadoJson))
                .andReturn().getResponse().getContentAsString();
        Long usuarioBuscadoId = objectMapper.readTree(responseCadastro).get("id").asLong();

        mockMvc.perform(get("/usuarios/" + usuarioBuscadoId)
                        .header("Authorization", tokenTreinador))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioBuscadoId))
                .andExpect(jsonPath("$.email").value("buscado@email.com"));
    }

    @Test
    @DisplayName("Não deve buscar usuário por ID se não existir (com token válido)")
    void testNaoBuscarUsuarioPorIdSeNaoExistir() throws Exception {
        String tokenTreinador = obterTokenDeLogin("treinador.naobuscaid@email.com", "SenhaNaoBuscaId123!", TipoUsuario.TREINADOR);

        mockMvc.perform(get("/usuarios/999999")
                        .header("Authorization", tokenTreinador))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Não deve buscar usuário por ID sem token")
    void testNaoBuscarUsuarioPorIdSemToken() throws Exception {
        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void testAtualizarUsuarioComSucesso() throws Exception {
        String tokenTreinador = obterTokenDeLogin("treinador.atualiza@email.com", "SenhaAtualiza123!", TipoUsuario.TREINADOR);

        String usuarioParaAtualizarJson = "{\"nomeCompleto\":\"Usuario Antigo\","
                + "\"email\":\"antigo@email.com\","
                + "\"senha\":\"SenhaAntiga123!\","
                + "\"dataNascimento\":\"1990-01-01\","
                + "\"genero\":\"Masculino\","
                + "\"alturaCm\":180,"
                + "\"pesoKg\":80.0,"
                + "\"objetivoPrincipal\":\"Manter\","
                + "\"tipoUsuario\":\"ALUNO\"}";
        String responseCadastro = mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioParaAtualizarJson))
                .andReturn().getResponse().getContentAsString();
        Long usuarioIdParaAtualizar = objectMapper.readTree(responseCadastro).get("id").asLong();

        String usuarioAtualizadoJson = "{\"id\":" + usuarioIdParaAtualizar + ","
                + "\"nomeCompleto\":\"Usuario Novo Nome\","
                + "\"email\":\"novo.email@email.com\","
                + "\"senha\":\"NovaSenha123!\","
                + "\"dataNascimento\":\"1990-01-01\","
                + "\"genero\":\"Feminino\","
                + "\"alturaCm\":182,"
                + "\"pesoKg\":82.5,"
                + "\"objetivoPrincipal\":\"Hipertrofia\","
                + "\"tipoUsuario\":\"ALUNO\"}";

        mockMvc.perform(put("/usuarios/atualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioAtualizadoJson)
                        .header("Authorization", tokenTreinador))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto").value("Usuario Novo Nome"))
                .andExpect(jsonPath("$.email").value("novo.email@email.com"));

        Optional<Usuario> usuarioAtualizadoNoDb = usuarioRepository.findById(usuarioIdParaAtualizar);
        assertTrue(usuarioAtualizadoNoDb.isPresent());
        assertTrue(passwordEncoder.matches("NovaSenha123!", usuarioAtualizadoNoDb.get().getSenha()), "Senha deve ser a nova e correta");
    }

    @Test
    @DisplayName("Não deve atualizar usuário se ID não existir")
    void testNaoAtualizarUsuarioSeIdNaoExistir() throws Exception {
        String tokenTreinador = obterTokenDeLogin("treinador.atualiza.naoexiste@email.com", "SenhaAtualizaNaoExiste123!", TipoUsuario.TREINADOR);

        String usuarioInexistenteJson = "{\"id\":999999,"
                + "\"nomeCompleto\":\"Usuario Inexistente\","
                + "\"email\":\"inexistente@email.com\","
                + "\"senha\":\"SenhaInexistente123!\","
                + "\"dataNascimento\":\"2000-01-01\","
                + "\"genero\":\"Masculino\","
                + "\"alturaCm\":170,"
                + "\"pesoKg\":70.0,"
                + "\"objetivoPrincipal\":\"Teste\","
                + "\"tipoUsuario\":\"ALUNO\"}";

        mockMvc.perform(put("/usuarios/atualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioInexistenteJson)
                        .header("Authorization", tokenTreinador))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Não deve atualizar usuário com email já cadastrado por outro usuário")
    void testNaoAtualizarUsuarioComEmailDuplicado() throws Exception {
        String tokenTreinador = obterTokenDeLogin("treinador.emailduplicado@email.com", "SenhaEmailDuplicado123!", TipoUsuario.TREINADOR);

        String usuario1Json = "{\"nomeCompleto\":\"Usuario Um\","
                + "\"email\":\"usuario1@email.com\","
                + "\"senha\":\"Senha123!\","
                + "\"dataNascimento\":\"1990-01-01\","
                + "\"genero\":\"Masculino\","
                + "\"alturaCm\":170,"
                + "\"pesoKg\":70.0,"
                + "\"objetivoPrincipal\":\"Manter\","
                + "\"tipoUsuario\":\"ALUNO\"}";
        String responseCadastro = mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuario1Json))
                .andReturn().getResponse().getContentAsString();
        Long usuario1Id = objectMapper.readTree(responseCadastro).get("id").asLong();

        String usuario2Json = "{\"nomeCompleto\":\"Usuario Dois\","
                + "\"email\":\"usuario2@email.com\","
                + "\"senha\":\"Senha234!\","
                + "\"dataNascimento\":\"1991-02-02\","
                + "\"genero\":\"Feminino\","
                + "\"alturaCm\":160,"
                + "\"pesoKg\":55.0,"
                + "\"objetivoPrincipal\":\"Crescer\","
                + "\"tipoUsuario\":\"ALUNO\"}";
        mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuario2Json))
                .andExpect(status().isCreated());

        String usuarioAtualizadoComEmailDuplicadoJson = "{\"id\":" + usuario1Id + ","
                + "\"nomeCompleto\":\"Usuario Um Atualizado\","
                + "\"email\":\"usuario2@email.com\","
                + "\"senha\":\"SenhaUmNova!\","
                + "\"dataNascimento\":\"1990-01-01\","
                + "\"genero\":\"Masculino\","
                + "\"alturaCm\":170,"
                + "\"pesoKg\":70.0,"
                + "\"objetivoPrincipal\":\"Manter\","
                + "\"tipoUsuario\":\"ALUNO\"}";

        mockMvc.perform(put("/usuarios/atualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioAtualizadoComEmailDuplicadoJson)
                        .header("Authorization", tokenTreinador))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Não deve atualizar usuário sem token")
    void testNaoAtualizarUsuarioSemToken() throws Exception {
        String usuarioAtualizadoJson = "{\"id\":1,"
                + "\"nomeCompleto\":\"Nome Qualquer\","
                + "\"email\":\"qualquer@email.com\","
                + "\"senha\":\"SenhaQualquer123!\","
                + "\"dataNascimento\":\"2000-01-01\","
                + "\"genero\":\"Masculino\","
                + "\"alturaCm\":170,"
                + "\"pesoKg\":70.0,"
                + "\"objetivoPrincipal\":\"Teste\","
                + "\"tipoUsuario\":\"ALUNO\"}";

        mockMvc.perform(put("/usuarios/atualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioAtualizadoJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve desativar um usuário com sucesso via PUT")
    void testDesativarUsuarioComSucesso() throws Exception {
        String tokenTreinador = obterTokenDeLogin("treinador.desativa@email.com", "SenhaDesativa123!", TipoUsuario.TREINADOR);

        String usuarioParaDesativarJson = "{\"nomeCompleto\":\"Usuario Ativo\","
                + "\"email\":\"ativo@email.com\","
                + "\"senha\":\"SenhaAtivo123!\","
                + "\"dataNascimento\":\"1990-01-01\","
                + "\"genero\":\"Masculino\","
                + "\"alturaCm\":170,"
                + "\"pesoKg\":70.0,"
                + "\"objetivoPrincipal\":\"Manter\","
                + "\"tipoUsuario\":\"ALUNO\"}";
        String responseCadastro = mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioParaDesativarJson))
                .andReturn().getResponse().getContentAsString();
        Long usuarioIdParaDesativar = objectMapper.readTree(responseCadastro).get("id").asLong();

        String usuarioDesativadoJson = "{\"id\":" + usuarioIdParaDesativar + ","
                + "\"nomeCompleto\":\"Usuario Ativo\","
                + "\"email\":\"ativo@email.com\","
                + "\"senha\":\"SenhaAtivo123!\","
                + "\"dataNascimento\":\"1990-01-01\","
                + "\"genero\":\"Masculino\","
                + "\"alturaCm\":170,"
                + "\"pesoKg\":70.0,"
                + "\"objetivoPrincipal\":\"Manter\","
                + "\"tipoUsuario\":\"ALUNO\","
                + "\"ativo\":false}";

        mockMvc.perform(put("/usuarios/atualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioDesativadoJson)
                        .header("Authorization", tokenTreinador))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));

        Optional<Usuario> usuarioDesativadoNoDb = usuarioRepository.findById(usuarioIdParaDesativar);
        assertTrue(usuarioDesativadoNoDb.isPresent());
        assertFalse(usuarioDesativadoNoDb.get().isAtivo());
        assertNotNull(usuarioDesativadoNoDb.get().getDataDesativacao());
    }

    @Test
    @DisplayName("Não deve logar usuário inativo")
    void testNaoDeveLogarUsuarioInativo() throws Exception {
        String emailInativo = "inativo@email.com";
        String senhaInativo = "SenhaInativo123!";

        String tokenTreinador = obterTokenDeLogin("treinador.inativo@email.com", "SenhaTreinadorInativo123!", TipoUsuario.TREINADOR);

        String usuarioParaDesativarJson = "{\"nomeCompleto\":\"Usuario Inativo Teste\","
                + "\"email\":\""+emailInativo+"\","
                + "\"senha\":\""+senhaInativo+"\","
                + "\"dataNascimento\":\"1990-01-01\","
                + "\"genero\":\"Masculino\","
                + "\"alturaCm\":170,"
                + "\"pesoKg\":70.0,"
                + "\"objetivoPrincipal\":\"Manter\","
                + "\"tipoUsuario\":\"ALUNO\"}";
        String responseCadastro = mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioParaDesativarJson))
                .andReturn().getResponse().getContentAsString();
        Long usuarioIdInativo = objectMapper.readTree(responseCadastro).get("id").asLong();

        String usuarioDesativadoJson = "{\"id\":" + usuarioIdInativo + ","
                + "\"nomeCompleto\":\"Usuario Inativo Teste\","
                + "\"email\":\""+emailInativo+"\","
                + "\"senha\":\""+senhaInativo+"\","
                + "\"dataNascimento\":\"1990-01-01\","
                + "\"genero\":\"Masculino\","
                + "\"alturaCm\":170,"
                + "\"pesoKg\":70.0,"
                + "\"objetivoPrincipal\":\"Manter\","
                + "\"tipoUsuario\":\"ALUNO\","
                + "\"ativo\":false}";
        mockMvc.perform(put("/usuarios/atualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioDesativadoJson)
                        .header("Authorization", tokenTreinador))
                .andExpect(status().isOk());

        String loginJson = String.format("{\"email\":\"%s\",\"senha\":\"%s\"}", emailInativo, senhaInativo);
        mockMvc.perform(post("/usuarios/logar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized());
    }
}