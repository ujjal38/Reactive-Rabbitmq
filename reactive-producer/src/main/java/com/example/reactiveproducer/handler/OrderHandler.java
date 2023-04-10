package com.example.reactiveproducer.handler;

import com.example.reactiveproducer.config.RabbitConfig;
import com.example.reactiveproducer.dto.OrderDto;
import com.example.reactiveproducer.dto.event.Event;
import com.example.reactiveproducer.service.RabbitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderHandler {

    private final RabbitService rabbitService;

    public void handleOrder(OrderDto orderDto){
        rabbitService.sendMessage(setUpOrderEvent(orderDto));
    }

    private Event<OrderDto> setUpOrderEvent(OrderDto dto){
        return new Event<>(RabbitConfig.ORDER_QUEUE_EXCHANGE,
                RabbitConfig.ORDER_QUEUE,
                RabbitConfig.ORDER_QUEUE_ROUTING_KEY,
                dto);
    }
}
