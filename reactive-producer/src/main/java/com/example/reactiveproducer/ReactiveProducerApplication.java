package com.example.reactiveproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.reactiveproducer","com.example.commonreactivelib"})
public class ReactiveProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveProducerApplication.class, args);
    }

}
