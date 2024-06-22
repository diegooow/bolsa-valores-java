package com.example.Bolsa.Services;

import com.example.Bolsa.DTos.AcompanharAtivoDto;
import com.example.Bolsa.DTos.LoginResponseDto;
import com.example.Bolsa.DTos.UsuarioRequestDto;
import com.example.Bolsa.Models.Ativos;
import com.example.Bolsa.Models.LivroDeOfertas;
import com.example.Bolsa.Models.Usuario;
import com.example.Bolsa.Repositories.UsuarioRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<LoginResponseDto> Login(UsuarioRequestDto usuario) {
        Usuario user = usuarioRepository.findByNome(usuario.getNome());
        if (user != null && user.getSenha().equals(usuario.getSenha())) {
            LoginResponseDto logged = new LoginResponseDto();
            logged.setNome(user.getNome());
            logged.setId(user.getId());
            return Optional.of(logged);
        }
        return Optional.empty();
    }

    public Usuario RegisterUser(UsuarioRequestDto usuario) {
        Usuario newUser = new Usuario();
        newUser.setNome(usuario.getNome());
        newUser.setSenha(usuario.getSenha());
        usuarioRepository.save(newUser);
        return newUser;
    }

    @Transactional
    public boolean AcompanharAtivo(AcompanharAtivoDto acompanharAtivoDto) {
        int usuarioId = acompanharAtivoDto.getUsuarioId();
        String ativo = acompanharAtivoDto.getAtivo();
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return false;
        }
        usuario.adicionarAtivoAcompanhado(Ativos.valueOf(ativo.toUpperCase()));
        usuarioRepository.save(usuario);

        return true;
    }

    @Transactional
    public boolean NaoAcompanharAtivo(AcompanharAtivoDto acompanharAtivoDto) {
        int usuarioId = acompanharAtivoDto.getUsuarioId();
        String ativo = acompanharAtivoDto.getAtivo();
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return false;
        }
        usuario.removerAtivoAcompanhado(Ativos.valueOf(ativo.toUpperCase()));
        usuarioRepository.save(usuario);
        return true;
    }

    @Transactional
    public List<String> getAtivosAcompanhados(int usuarioId) {
        Usuario usuario = usuarioRepository.findByIdWithAtivosAcompanhados(usuarioId);
        if (usuario == null) {
            throw new RuntimeException("Usuario nao encontrado");
        }

        Hibernate.initialize(usuario.getAtivosAcompanhados());

        return usuario.getAtivosAcompanhados().stream()
                .map(ativosAcompanhado -> ativosAcompanhado.getAtivo().name())
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public String processarMudancaAtivo(LivroDeOfertas livroDeOfertas) {
        return String.format("O ativo %s sofreu alterações na ordem %s com valor %s e quantidade %s",
                livroDeOfertas.getAtivo(),
                livroDeOfertas.getOrdem(),
                livroDeOfertas.getValor(),
                livroDeOfertas.getQuantidade());
    }

}