package com.example.Bolsa.Services;

import com.example.Bolsa.DTos.LivroDeOfertasDto;
import com.example.Bolsa.Models.LivroDeOfertas;
import com.example.Bolsa.Repositories.LivroDeOfertasRepository;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivroDeOfertasService {
    @Autowired
    private LivroDeOfertasRepository livroDeOfertasRepository;

    public List<LivroDeOfertasDto> retornarOfertasUsuario(int id) {
        List<LivroDeOfertas> ofertas = livroDeOfertasRepository.findAllByUsuarioId(id);
        if (ofertas == null || ofertas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existem ofertas para esse usuário");
        }
        return ofertas.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<LivroDeOfertasDto> retornarOfertas() {
        List<LivroDeOfertas> ofertas = livroDeOfertasRepository.findAll();
        return ofertas.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private LivroDeOfertasDto mapToDto(LivroDeOfertas livroDeOfertas) {
        LivroDeOfertasDto dto = new LivroDeOfertasDto();
        dto.setUsuarioId(livroDeOfertas.getUsuario().getId());
        dto.setAtivo(String.valueOf(livroDeOfertas.getAtivo()));
        dto.setValor(livroDeOfertas.getValor());
        dto.setQuantidade(livroDeOfertas.getQuantidade());
        dto.setOrdem(String.valueOf(livroDeOfertas.getOrdem()));
        dto.setFoiRelizado(livroDeOfertas.isFoiRealizado() ? "Realizado": "Não Realizado");
        return dto;
    }
}
