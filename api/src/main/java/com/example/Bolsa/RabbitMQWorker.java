package com.example.Bolsa;

import com.example.Bolsa.Controllers.NotificacaoController;
import com.example.Bolsa.DTos.LivroDeOfertasDto;
import com.example.Bolsa.Models.Ativos;
import com.example.Bolsa.Models.LivroDeOfertas;
import com.example.Bolsa.Models.Ordem;
import com.example.Bolsa.Models.Usuario;
import com.example.Bolsa.Models.Notificacao;
import com.example.Bolsa.Repositories.UsuarioRepository;
import com.example.Bolsa.Services.TransacaoService;
import com.example.Bolsa.Services.UsuarioService;
import com.example.Bolsa.Services.NotificacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitMQWorker {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQWorker.class);

    @Autowired
    private TransacaoService transacaoService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private NotificacaoController notificacaoController;
    @Autowired
    private NotificacaoService notificacaoService;

    private static final String EXCHANGE_NAME = "ativosExchange";

    public void startWorker() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("jackal-01.rmq.cloudamqp.com");
        factory.setPort(5672);
        factory.setUsername("bsrosyvg");
        factory.setPassword("5JEUO6PoXXqBFUmxQ1LIM8p0PaZJdnX5");
        factory.setVirtualHost("bsrosyvg");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String rabbitMQQueue = "Fila1";
            channel.queueDeclare(rabbitMQQueue, true, false, false, null);
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            logger.info("Worker reading messages!");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        String json = new String(body, "UTF-8");
                        ObjectMapper mapper = new ObjectMapper();
                        LivroDeOfertasDto livroDeOfertasDto = mapper.readValue(json, LivroDeOfertasDto.class);
                        LivroDeOfertas livroDeOfertas = new LivroDeOfertas();
                        Optional<Usuario> usuarioOptional = usuarioRepository.findById(livroDeOfertasDto.getUsuarioId());
                        usuarioOptional.ifPresent(livroDeOfertas::setUsuario);
                        livroDeOfertas.setQuantidade(livroDeOfertasDto.getQuantidade());
                        livroDeOfertas.setValor(livroDeOfertasDto.getValor());
                        livroDeOfertas.setAtivo(Ativos.valueOf(livroDeOfertasDto.getAtivo()));
                        livroDeOfertas.setOrdem(Ordem.valueOf(livroDeOfertasDto.getOrdem()));
                        if (livroDeOfertas != null) {
                            boolean sucesso = false;
                            if (livroDeOfertas.getOrdem() == Ordem.COMPRA) {
                                sucesso = transacaoService.ordemCompra(livroDeOfertas);
                                if (!sucesso) {
                                    logger.info("Não foi possível realizar a compra neste momento.");
                                }
                            } else if (livroDeOfertas.getOrdem() == Ordem.VENDA) {
                                sucesso = transacaoService.ordemVenda(livroDeOfertas);
                                if (!sucesso) {
                                    logger.info("Não foi possível realizar a venda neste momento.");
                                }
                            }
                                String mensagem = usuarioService.processarMudancaAtivo(livroDeOfertas);
                                for (Usuario usuario : usuarioRepository.findAllWithAtivosAcompanhados()) {
                                    if (usuario.getAtivosAcompanhados().stream().anyMatch(ua -> ua.getAtivo().equals(livroDeOfertas.getAtivo()))) {
                                        channel.basicPublish(EXCHANGE_NAME, livroDeOfertas.getAtivo().toString(), null, mensagem.getBytes());
                                        notificacaoController.notifyUsers(livroDeOfertas.getAtivo().toString(), mensagem);

                                        Notificacao notificacao = new Notificacao();
                                        notificacao.setUsuarioId(usuario.getId());
                                        notificacao.setMensagem(mensagem);
                                        notificacaoService.save(notificacao);

                                        logger.info("Mensagem enviada para o tópico {}: {}", livroDeOfertas.getAtivo().toString(), mensagem);
                                    }
                                }
                        } else {
                            logger.warn("livroDeOfertas é nulo. Ignorando mensagem.");
                        }

                    } catch (Exception e) {
                        logger.error("Error processing message", e);
                    }
                }
            };

            channel.basicConsume(rabbitMQQueue, true, consumer);
            Thread.sleep(1000); // Aguarda 1 segundo antes de verificar novamente a fila

        } catch (IOException | TimeoutException | InterruptedException e) {
            logger.error("Error connecting to RabbitMQ", e);
        }
    }

    @PostConstruct
    public void init() {
        Thread thread = new Thread(this::startWorker);
        thread.start();
    }
}
