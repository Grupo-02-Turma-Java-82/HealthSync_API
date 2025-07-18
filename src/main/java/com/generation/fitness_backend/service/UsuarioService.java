package com.generation.fitness_backend.service;

import java.util.List;
import java.util.Optional;

import com.generation.fitness_backend.enums.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.model.UsuarioLogin;
import com.generation.fitness_backend.repository.UsuarioRepository;
import com.generation.fitness_backend.security.JwtService;

@Service
public class UsuarioService { //logica de autent. e criptografia de senha

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public List<Usuario> getAll() { //buscar todos
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getById(Long id) { // busca pelo ID
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> getByEmail(String email) { // busca pelo email
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return Optional.empty();
        }
        usuario.setSenha(criptografarSenha(usuario.getSenha()));

        //define a role padrao para novos cadastros se o tipousuario n for especificado = aluno
        if (usuario.getTipoUsuario() == null) {
            usuario.setTipoUsuario(TipoUsuario.ALUNO);
        }
        usuario.setAtivo(true);

        return Optional.of(usuarioRepository.save(usuario));
    }

    public Optional<Usuario> atualizarUsuario(Usuario usuario) {

        if (usuario.getId() == null || usuarioRepository.findById(usuario.getId()).isEmpty()) {
            return Optional.empty();
        }
        Optional<Usuario> buscaUsuarioExistente = usuarioRepository.findById(usuario.getId());

        Optional<Usuario> buscaUsuarioPorEmail = usuarioRepository.findByEmail(usuario.getEmail());
        if (buscaUsuarioPorEmail.isPresent() && !buscaUsuarioPorEmail.get().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado por outro usuário!");
        }
        Usuario usuarioExistente = buscaUsuarioExistente.get();

        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            usuarioExistente.setSenha(criptografarSenha(usuario.getSenha()));
        } else {
            //dx a senha antiga se nenhuma nova for fornecida
            usuario.setSenha(usuarioExistente.getSenha());
        }

        if (usuario.getTipoUsuario() != null) {
            usuarioExistente.setTipoUsuario(usuario.getTipoUsuario());
        }
        usuarioExistente.setAtivo(usuario.isAtivo());

        usuarioExistente.setNomeCompleto(usuario.getNomeCompleto());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setDataNascimento(usuario.getDataNascimento());
        usuarioExistente.setGenero(usuario.getGenero());
        usuarioExistente.setAlturaCm(usuario.getAlturaCm());
        usuarioExistente.setPesoKg(usuario.getPesoKg());
        usuarioExistente.setObjetivoPrincipal(usuario.getObjetivoPrincipal());
        usuarioExistente.setUrlImagem(usuario.getUrlImagem());
        usuarioExistente.setDataDesativacao(usuario.getDataDesativacao());

        return Optional.of(usuarioRepository.save(usuarioExistente));
    }

    public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {
        if (usuarioLogin.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "credenciais de login invalidas.");
        }
        var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getEmail(),
                usuarioLogin.get().getSenha());
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(credenciais);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuario ou senha invalidos!", e);
        }

        if (authentication.isAuthenticated()) {
            //busca o usuario completo no banco de dados pelo email
            Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioLogin.get().getEmail());

            if (usuario.isPresent()) {
                //verifica se o usuario esta ativo
                if (!usuario.get().isAtivo()) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuario inativo!");
                }

                //gera o token jwt incluindo a role
                String token = gerarToken(usuario.get().getEmail(), usuario.get().getTipoUsuario()); //passa email e role do usuario do banco

                UsuarioLogin retornoLogin = new UsuarioLogin(usuario.get()); //construtor q recebe Usuario
                retornoLogin.setToken(token);

                return Optional.of(retornoLogin); //retorna o optional usuariologin populado
            }
        }
        return Optional.empty();
    }
    //todo: calc imc

    public void deleteById(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");
        }
        usuarioRepository.deleteById(id);
    }

    private String criptografarSenha(String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(senha);
    }

    private String gerarToken(String email, TipoUsuario tipoUsuario) {
        return "Bearer " + jwtService.generateToken(email, tipoUsuario);
    }
}
