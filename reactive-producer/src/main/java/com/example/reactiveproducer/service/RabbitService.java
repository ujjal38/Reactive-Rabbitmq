package com.example.reactiveproducer.service;

import com.example.reactiveproducer.dto.OrderDto;
import com.example.reactiveproducer.dto.event.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import reactor.core.publisher.Flux;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

import static reactor.rabbitmq.ResourcesSpecification.*;

@Service
@Slf4j
public class RabbitService {

    private final Sender sender;

    public RabbitService(Sender sender) {
        this.sender = sender;
    }

    public <T> void sendMessage(Event<T> message) {
        bindSender(message);
        sender.sendWithPublishConfirms(getOutBoundMessage(message))
                .subscribe(outboundMessageResult -> {
                    if (outboundMessageResult.isAck()) {
                        log.info("Message is ack by broker with exchange {} and body {}", outboundMessageResult.getOutboundMessage().getExchange(), outboundMessageResult.getOutboundMessage());
                    }
                });

    }

    private <T> Publisher<OutboundMessage> getOutBoundMessage(Event<T> message) {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(message.getBody());

            log.info("Serializing Message Body {}",json);

            byte[] orderSerialized = SerializationUtils.serialize(json);
            //Outbound Message that will be sent by the Sender
            assert orderSerialized != null;
            return Flux.just(new OutboundMessage(message.getExchange()
                    , message.getRoutingKey(),
                    orderSerialized));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private <T> void bindSender(Event<T> message) {
        sender.declare(exchange(message.getExchange()))
                .then(sender.declare(queue(message.getQueueName())))
                .then(sender.bind(binding(message.getExchange(), message.getRoutingKey(), message.getQueueName())))
                .subscribe();
    }
}
