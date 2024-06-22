package com.example.Bolsa.Models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transacao {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "compra_id")
    private LivroDeOfertas livroDeOfertasCompra;

    @ManyToOne
    @JoinColumn(name = "venda_id")
    private LivroDeOfertas livroDeOfertasVenda;

    private int quantidade;

    public Transacao(LivroDeOfertas livroDeOfertasCompra, LivroDeOfertas livroDeOfertasVenda, int quantidade) {
        this.livroDeOfertasCompra = livroDeOfertasCompra;
        this.livroDeOfertasVenda = livroDeOfertasVenda;
        this.quantidade = quantidade;
    }

}
