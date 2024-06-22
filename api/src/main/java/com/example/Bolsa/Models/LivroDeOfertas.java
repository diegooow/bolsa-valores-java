package com.example.Bolsa.Models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class LivroDeOfertas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Usuario usuario;

    private int quantidade;
    private float valor;
    private Ativos ativo;
    private Ordem ordem;
    private boolean foiRealizado;
    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @OneToMany(mappedBy = "livroDeOfertasCompra")
    private List<Transacao> compras;

    @OneToMany(mappedBy = "livroDeOfertasVenda")
    private List<Transacao> vendas;

    @Override
    public String toString() {
        return "LivroDeOfertas{" +
                "id=" + id +
                ", quantidade=" + quantidade +
                ", valor=" + valor +
                ", corretora=" + ativo +
                ", tipo=" + ordem +
                ", foiRealizado=" + foiRealizado +
                ", createdAt=" + createdAt +
                '}';
    }
}