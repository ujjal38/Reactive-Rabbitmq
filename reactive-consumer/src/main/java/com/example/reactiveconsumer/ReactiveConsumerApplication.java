package com.example.reactiveconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.reactiveconsumer","com.example.commonreactivelib"})
public class ReactiveConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveConsumerApplication.class, args);
    }

}
