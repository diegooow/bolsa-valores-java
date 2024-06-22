package com.example.Bolsa.Repositories;

import com.example.Bolsa.Models.LivroDeOfertas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivroDeOfertasRepository extends JpaRepository<LivroDeOfertas, Integer> {
    @Query("SELECT l FROM LivroDeOfertas l WHERE l.usuario.id = ?1")
    List<LivroDeOfertas> findAllByUsuarioId(int id);
}
