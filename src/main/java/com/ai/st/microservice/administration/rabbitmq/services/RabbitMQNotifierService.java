package com.ai.st.microservice.administration.rabbitmq.services;

import com.ai.st.microservice.common.dto.notifier.MicroserviceNotificationDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQNotifierService {

    @Value("${st.rabbitmq.queueNotifications.exchange}")
    public String exchangeFilesName;

    @Value("${st.rabbitmq.queueNotifications.routingkey}")
    public String routingkeyFilesName;

    private final AmqpTemplate rabbitTemplate;

    public RabbitMQNotifierService(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNotification(String subject, String body, String email, Long userCode) {

        MicroserviceNotificationDto data = new MicroserviceNotificationDto();
        data.setEmail(email);
        data.setSubject(subject);
        data.setMessage(body);
        data.setUserCode(userCode);
        data.setType("change-password");
        data.setStatus(1);

        rabbitTemplate.convertAndSend(exchangeFilesName, routingkeyFilesName, data);
    }

}