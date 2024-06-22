package com.example.Bolsa.Controllers;

import com.example.Bolsa.Models.Notificacao;
import com.example.Bolsa.Services.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/nao-lidas/{usuarioId}")
    public List<Notificacao> getNotificacoesNaoLidas(@PathVariable int usuarioId) {
        return notificacaoService.getNotificacoesNaoLidas(usuarioId);
    }

    @PutMapping("/marcar-como-lida/{notificacaoId}")
    public void marcarComoLida(@PathVariable int notificacaoId) {
        notificacaoService.marcarComoLida(notificacaoId);
    }

    public void notifyUsers(String ativo, String mensagem) {
        messagingTemplate.convertAndSend("/topic/" + ativo, mensagem);
    }
}
