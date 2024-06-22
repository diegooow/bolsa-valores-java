package com.example.Bolsa.DTos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AcompanharAtivoDto {
    public int usuarioId;
    public String ativo;
}
