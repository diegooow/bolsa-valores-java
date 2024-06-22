package com.example.Bolsa.Repositories;

import com.example.Bolsa.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT u FROM Usuario u WHERE u.nome = ?1")
    Usuario findByNome(String nome);
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.ativosAcompanhados WHERE u.id = :usuarioId")
    Usuario findByIdWithAtivosAcompanhados(@Param("usuarioId") int usuarioId);
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.ativosAcompanhados")
    List<Usuario> findAllWithAtivosAcompanhados();
}