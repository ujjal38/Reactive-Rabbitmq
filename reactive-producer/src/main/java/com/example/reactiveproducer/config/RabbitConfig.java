package com.example.reactiveproducer.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.*;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class RabbitConfig {

    public static String ORDER_QUEUE;

    public static String ORDER_QUEUE_EXCHANGE;

    public static String ORDER_QUEUE_ROUTING_KEY;

    private final String username;
    private final String password;
    private final String host;
    private final int port;

    public RabbitConfig(@Value("${spring.rabbitmq.username}") String username,
                                @Value("${spring.rabbitmq.password}") String password,
                                @Value("${spring.rabbitmq.host}") String host,
                                @Value("${spring.rabbitmq.port}") int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    @Autowired
    public  void setOrderQueue(@Value("${rabbit-config.order.queue.name}")  String orderQueue) {
        ORDER_QUEUE = orderQueue;
    }

    @Autowired
    public  void setOrderQueueExchange(@Value("${rabbit-config.order.exchange}")  String orderQueueExchange) {
        ORDER_QUEUE_EXCHANGE = orderQueueExchange;
    }

    @Autowired
    public  void setOrderQueueRoutingKey(@Value("${rabbit-config.order.queue.routing-key}")  String orderQueueRoutingKey) {
        ORDER_QUEUE_ROUTING_KEY = orderQueueRoutingKey;
    }

    @Bean
    public Declarables createRabbitMQOrderSchema() {
        return new Declarables(
                new DirectExchange(ORDER_QUEUE_EXCHANGE, true, false, null),
                new Queue(ORDER_QUEUE),
                new Binding(ORDER_QUEUE, Binding.DestinationType.QUEUE, ORDER_QUEUE_EXCHANGE, ORDER_QUEUE_ROUTING_KEY, null));
    }

    @Bean
    public Mono<Connection> connectionFactory() throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        String uri  = String.format("amqp://%s:%d/", host, port);
        connectionFactory.setUri(uri);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.useNio();
        return Mono.fromCallable(
                        ()->connectionFactory.newConnection("reactive-sender"))
                .cache();

    }


    @Bean
    public SenderOptions senderOptions(Mono<Connection> connectionMono) {
        return new SenderOptions()
                .connectionMono(connectionMono)
                .resourceManagementScheduler(Schedulers.boundedElastic());
    }


    @Bean
    public Sender sender(SenderOptions senderOptions) {
        return RabbitFlux.createSender(senderOptions);
    }

}
