package com.example.Bolsa.Services;

import com.example.Bolsa.Models.Notificacao;
import com.example.Bolsa.Repositories.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    public List<Notificacao> getNotificacoesNaoLidas(int usuarioId) {
        return notificacaoRepository.findByUsuarioIdAndLidaFalse(usuarioId);
    }

    public void marcarComoLida(int notificacaoId) {
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Notificação não encontrada"));
        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);
    }

    public void save(Notificacao notificacao) {
        notificacaoRepository.save(notificacao);
    }
}
