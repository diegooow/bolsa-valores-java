package com.example.Bolsa.Controllers;

import com.example.Bolsa.DTos.AcompanharAtivoDto;
import com.example.Bolsa.DTos.LoginResponseDto;
import com.example.Bolsa.DTos.UsuarioRequestDto;
import com.example.Bolsa.Models.Usuario;
import com.example.Bolsa.Repositories.UsuarioRepository;
import com.example.Bolsa.Services.UsuarioService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Bolsa.DTos.LoginResponseDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "http://127.0.0.1:5500/")
@OpenAPIDefinition
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registro(@RequestBody UsuarioRequestDto usuario) {
        Usuario registeredUser = usuarioService.RegisterUser(usuario);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioRequestDto usuario) {
        Optional<LoginResponseDto> loggedInUser = usuarioService.Login(usuario);
        if (loggedInUser.isPresent()) {
            return ResponseEntity.ok(loggedInUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/acompanhar")
    public ResponseEntity<Boolean> acompanharAtivos(@RequestBody AcompanharAtivoDto acompanharAtivoDto) {
        usuarioService.AcompanharAtivo(acompanharAtivoDto);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/nao-acompanhar")
    public ResponseEntity<Boolean> naoAcompanharAtivos(@RequestBody AcompanharAtivoDto acompanharAtivoDto) {
        usuarioService.NaoAcompanharAtivo(acompanharAtivoDto);
        return ResponseEntity.ok(true);
    }

    @GetMapping("{usuarioId}/ativos-acompanhados")
    public ResponseEntity<?> getAtivosAcompanhados(@PathVariable int usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(user -> {
                    List<String> ativos = usuarioService.getAtivosAcompanhados(usuarioId);
                    return ResponseEntity.ok(ativos);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(List.of("O usuário não foi encontrado")));
    }
}