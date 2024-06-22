package com.example.Bolsa.DTos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LivroDeOfertasDto {
    private int usuarioId;
    private int quantidade;
    private float valor;
    private String ativo;
    private String ordem;
    private String foiRelizado;
}
