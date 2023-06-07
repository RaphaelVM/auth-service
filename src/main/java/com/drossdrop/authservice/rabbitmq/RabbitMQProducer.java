package com.drossdrop.authservice.rabbitmq;

import com.drossdrop.authservice.dto.UserRabbitMQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUser(UserRabbitMQ user) {
        LOGGER.info(String.format("Sending message...-> %s", user.toString()));
        rabbitTemplate.convertAndSend("user_exchange", "user_routing_json_key", user);
    }
}
