package com.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sanjaya on 12/4/16.
 */
@Component
public class MessageProducer<T> {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(String exchange, String routingKey, T t) {
        System.out.println("Publishing message" + t);
        rabbitTemplate.convertAndSend(exchange, routingKey, t);
    }
}
