package com.example.Bolsa.Services;

import com.example.Bolsa.Models.LivroDeOfertas;
import com.example.Bolsa.Models.Ordem;
import com.example.Bolsa.Models.Transacao;
import com.example.Bolsa.Repositories.LivroDeOfertasRepository;
import com.example.Bolsa.Repositories.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private LivroDeOfertasRepository livroDeOfertasRepository;

    public boolean ordemCompra(LivroDeOfertas ofertaCompra) {
        List<LivroDeOfertas> todasOfertas = livroDeOfertasRepository.findAll();

        List<LivroDeOfertas> ofertasVenda = todasOfertas.stream()
                .filter(oferta -> !oferta.isFoiRealizado() && oferta.getOrdem() == Ordem.VENDA
                        && oferta.getAtivo().equals(ofertaCompra.getAtivo())
                        && oferta.getQuantidade() >= ofertaCompra.getQuantidade()
                        && oferta.getValor() <= ofertaCompra.getValor())
                .sorted(Comparator.comparing(LivroDeOfertas::getValor)
                        .thenComparing(LivroDeOfertas::getCreatedAt))
                .toList();

        if (!ofertasVenda.isEmpty()) {
            LivroDeOfertas ofertaVenda = ofertasVenda.get(0);
            int quantidadeTransacionada = Math.min(ofertaCompra.getQuantidade(), ofertaVenda.getQuantidade());

            if (quantidadeTransacionada > 0) {
                ofertaVenda.setQuantidade(ofertaVenda.getQuantidade() - quantidadeTransacionada);
                ofertaCompra.setQuantidade(ofertaCompra.getQuantidade() - quantidadeTransacionada);

                if (ofertaCompra.getQuantidade() == 0) ofertaCompra.setFoiRealizado(true);
                if (ofertaVenda.getQuantidade() == 0) ofertaVenda.setFoiRealizado(true);

                livroDeOfertasRepository.save(ofertaCompra);
                transacaoRepository.save(new Transacao(ofertaCompra, ofertaVenda, quantidadeTransacionada));
                livroDeOfertasRepository.save(ofertaVenda);
                return true;
            }
        }

        livroDeOfertasRepository.save(ofertaCompra);
        return false;
    }

    public boolean ordemVenda(LivroDeOfertas ofertaVenda) {
        List<LivroDeOfertas> todasOfertas = livroDeOfertasRepository.findAll();

        List<LivroDeOfertas> ofertasCompra = todasOfertas.stream()
                .filter(oferta -> !oferta.isFoiRealizado() && oferta.getOrdem() == Ordem.COMPRA
                        && oferta.getQuantidade() >= ofertaVenda.getQuantidade()
                        && oferta.getValor() >= ofertaVenda.getValor()
                        && oferta.getAtivo().equals(ofertaVenda.getAtivo()))
                .sorted(Comparator.comparing(LivroDeOfertas::getValor).reversed()
                        .thenComparing(LivroDeOfertas::getCreatedAt))
                .toList();

        if (!ofertasCompra.isEmpty()) {
            LivroDeOfertas ofertaCompra = ofertasCompra.get(0);
            int quantidadeVenda = Math.min(ofertaVenda.getQuantidade(), ofertaCompra.getQuantidade());

            ofertaCompra.setQuantidade(ofertaCompra.getQuantidade() - quantidadeVenda);
            ofertaVenda.setQuantidade(ofertaVenda.getQuantidade() - quantidadeVenda);

            if (ofertaCompra.getQuantidade() == 0) {
                ofertaCompra.setFoiRealizado(true);
                livroDeOfertasRepository.save(ofertaCompra);
            }

            if (ofertaVenda.getQuantidade() > 0) {
                livroDeOfertasRepository.save(ofertaVenda);
            } else {
                ofertaVenda.setFoiRealizado(true);
                livroDeOfertasRepository.save(ofertaVenda);
            }

            transacaoRepository.save(new Transacao(ofertaCompra, ofertaVenda, quantidadeVenda));
            return true;
        }

        livroDeOfertasRepository.save(ofertaVenda);
        return false;
    }
}