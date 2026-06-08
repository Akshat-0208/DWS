package com.dws.java_schedulers.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.dws.java_schedulers.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.QUEUE_NAME,
                message
        );

        System.out.println(
                "Message sent to queue: " + message
        );
    }
}