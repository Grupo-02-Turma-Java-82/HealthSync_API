package com.generation.fitness_backend.service;

import java.util.List;
import java.util.Optional;

import com.generation.fitness_backend.enums.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.fitness_backend.model.Usuario;
import com.generation.fitness_backend.model.UsuarioLogin;
import com.generation.fitness_backend.repository.UsuarioRepository;
import com.generation.fitness_backend.security.JwtService;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> getByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return Optional.empty();
        }
        usuario.setSenha(criptografarSenha(usuario.getSenha()));

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

    public Optional<UsuarioLogin> autenticarUsuario(UsuarioLogin usuarioLogin) {
        var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.getEmail(),
                usuarioLogin.getSenha());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(credenciais);
        } catch (Exception e) {
            return Optional.empty();
        }

        if (authentication.isAuthenticated()) {
            Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioLogin.getEmail());

            if (usuario.isPresent()) {
                if (!usuario.get().isAtivo()) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário inativo!");
                }

                String token = gerarToken(usuario.get().getEmail(), usuario.get().getTipoUsuario());

                UsuarioLogin retornoLogin = new UsuarioLogin();
                retornoLogin.setId(usuario.get().getId());
                retornoLogin.setNomeCompleto(usuario.get().getNomeCompleto());
                retornoLogin.setEmail(usuario.get().getEmail());
                retornoLogin.setTipoUsuario(usuario.get().getTipoUsuario());
                retornoLogin.setToken(token);

                return Optional.of(retornoLogin);
            }
        }
        return Optional.empty();
    }

    public void deleteById(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");
        }
        usuarioRepository.deleteById(id);
    }

    private String criptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    private String gerarToken(String email, TipoUsuario tipoUsuario) {
        return "Bearer " + jwtService.generateToken(email, tipoUsuario);
    }
}