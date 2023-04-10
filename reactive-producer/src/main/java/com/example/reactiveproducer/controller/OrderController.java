package com.example.reactiveproducer.controller;

import com.example.reactiveproducer.dto.OrderDto;
import com.example.reactiveproducer.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public Mono<OrderDto> createOrder(@RequestBody OrderDto dto){
        return orderService.sendOrder(dto);
    }
}
