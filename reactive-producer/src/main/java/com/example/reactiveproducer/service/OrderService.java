package com.example.reactiveproducer.service;

import com.example.reactiveproducer.dto.OrderDto;
import com.example.reactiveproducer.handler.OrderHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderHandler orderHandler;

    public Mono<OrderDto> sendOrder(OrderDto dto){
        //business or database logic
         orderHandler.handleOrder(dto);
         return Mono.just(dto);
    }
}
