package com.example.reactiveproducer.dto.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event<T>  {


    private final String exchange;
    private  final String queueName;
    private final String routingKey;
    private final T body;

    public Event(String exchange, String queueName, String routingKey, T body) {
        this.exchange = exchange;
        this.queueName = queueName;
        this.routingKey = routingKey;
        this.body = body;
    }
}
