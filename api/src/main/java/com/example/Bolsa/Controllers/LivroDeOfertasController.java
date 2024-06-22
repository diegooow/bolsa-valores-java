package com.example.Bolsa.Controllers;

import com.example.Bolsa.DTos.LivroDeOfertasDto;
import com.example.Bolsa.Services.LivroDeOfertasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LivroDeOfertasController {

    @Autowired
    private LivroDeOfertasService livroDeOfertasService;

    @GetMapping("/ofertas/{id}")
    public List<LivroDeOfertasDto> retornarOfertasUsuario(@PathVariable int id) {
        return livroDeOfertasService.retornarOfertasUsuario(id);
    }
    @GetMapping("/ofertas")
    public List<LivroDeOfertasDto> retornarTodasOfertas() {
        return livroDeOfertasService.retornarOfertas();
    }
}