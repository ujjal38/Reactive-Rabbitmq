package com.example.reactiveconsumer.service;

import com.example.reactiveconsumer.handler.OrderListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderListener orderListener;

//    private void processOrder(){
//        orderListener.receiveOrder()
//                .map(orderDto -> {
//                    System.out.println(orderDto.toString());
//                    return Mono.empty();
//                });
//    }
}
