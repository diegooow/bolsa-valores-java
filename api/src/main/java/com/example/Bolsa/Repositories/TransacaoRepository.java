package com.example.Bolsa.Repositories;

import com.example.Bolsa.Models.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Integer> {
}
