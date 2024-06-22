package com.example.Bolsa.Repositories;

import com.example.Bolsa.Models.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
    List<Notificacao> findByUsuarioIdAndLidaFalse(int usuarioId);
}
