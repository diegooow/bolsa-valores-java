package com.example.Bolsa.Controllers;

import com.example.Bolsa.DTos.LivroDeOfertasDto;
import com.example.Bolsa.Models.LivroDeOfertas;
import com.example.Bolsa.Services.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {

    private final TransacaoService transacaoService;

    @Autowired
    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/enviarOrdem")
    public ResponseEntity<Boolean> ordemCompra(@RequestBody LivroDeOfertasDto oferta) {
        try {
            enviarMensagemParaRabbitMQ(oferta);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    private void enviarMensagemParaRabbitMQ(LivroDeOfertasDto livroDeOfertasDto) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("jackal-01.rmq.cloudamqp.com");
        factory.setPort(5672);
        factory.setUsername("bsrosyvg");
        factory.setPassword("5JEUO6PoXXqBFUmxQ1LIM8p0PaZJdnX5");
        factory.setVirtualHost("bsrosyvg");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare("Fila1", true, false, false, null);
            ObjectMapper mapper = new ObjectMapper();
            String mensagem = mapper.writeValueAsString(livroDeOfertasDto);
            channel.basicPublish("", "Fila1", null, mensagem.getBytes());
        }
    }
}