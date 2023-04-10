package com.example.reactiveconsumer.service;
import com.example.reactiveconsumer.dto.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;
import javax.annotation.PreDestroy;


@Service
@Slf4j
public class RabbitService {

        private final Receiver receiver;
        private final ObjectMapper mapper = new ObjectMapper();

        public RabbitService(Receiver receiver) {
            this.receiver = receiver;
        }


    // Make sure the connection before the program is finished
    @PreDestroy
    private void close() {
        receiver.close();
    }

    //Consume messages from the queue

    public <T> Disposable consume(String queueName, Class<T> dtoClass){

        return receiver.consumeAutoAck(queueName)
                .flatMap(m->{
                    String json = (String) SerializationUtils.deserialize(m.getBody());
                    log.info("Received Json Value {}",json);
                    ObjectMapper mapper = new ObjectMapper();
                    T dto;

                    try {
                        dto =  mapper.readValue(json, dtoClass);
                        return Flux.just(dto);

                    } catch (JsonProcessingException e) {
                        return Flux.error(new RuntimeException());
                    }
                }).subscribe();

    }

}
