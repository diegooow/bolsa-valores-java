package com.example.Bolsa.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    @JsonIgnore
    private String senha;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AtivosAcompanhados> ativosAcompanhados = new HashSet<>();

    public void adicionarAtivoAcompanhado(Ativos ativo) {
        Hibernate.initialize(ativosAcompanhados);
        AtivosAcompanhados ativosAcompanhado = new AtivosAcompanhados();
        ativosAcompanhado.setUsuario(this);
        ativosAcompanhado.setAtivo(ativo);
        ativosAcompanhados.add(ativosAcompanhado);
    }

    public void removerAtivoAcompanhado(Ativos ativo) {
        Hibernate.initialize(ativosAcompanhados);
        ativosAcompanhados.removeIf(ativosAcompanhado -> ativosAcompanhado.getAtivo().equals(ativo) && ativosAcompanhado.getUsuario().equals(this));
    }
}
