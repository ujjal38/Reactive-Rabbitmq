package com.example.reactiveconsumer.handler;
import com.example.reactiveconsumer.config.RabbitConfig;
import com.example.reactiveconsumer.dto.OrderDto;
import com.example.reactiveconsumer.service.RabbitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderListener {

    private final RabbitService rabbitService;

    @PostConstruct
    private void init(){
        receiveOrder();
    }

    public void receiveOrder(){
         rabbitService.consume(
                RabbitConfig.ORDER_QUEUE,
                OrderDto.class
        );
    }

}
