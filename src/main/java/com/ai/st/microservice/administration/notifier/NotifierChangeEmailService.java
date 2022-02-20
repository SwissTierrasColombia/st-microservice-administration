package com.ai.st.microservice.administration.notifier;

import com.ai.st.microservice.administration.rabbitmq.services.RabbitMQNotifierService;
import com.ai.st.microservice.administration.thymeleaf.ThymeleafRenderService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotifierChangeEmailService {

    private final static String CHANGE_PASSWORD_FILE_TEMPLATE = "password_changed";

    private final static String NOTIFICATION_SUBJECT = "Se ha cambiado la contraseña";

    private final ThymeleafRenderService thymeleafRenderService;
    private final RabbitMQNotifierService rabbitMQNotifierService;

    public NotifierChangeEmailService(ThymeleafRenderService thymeleafRenderService,
                                      RabbitMQNotifierService rabbitMQNotifierService) {
        this.thymeleafRenderService = thymeleafRenderService;
        this.rabbitMQNotifierService = rabbitMQNotifierService;
    }

    public void sendNotification(Long userId, String email) {

        Map<String, Object> data = new HashMap<>();
//        data.put("userId", userId);

        String bodyMessage = thymeleafRenderService.parse(CHANGE_PASSWORD_FILE_TEMPLATE, data);

        rabbitMQNotifierService.sendNotification(NOTIFICATION_SUBJECT, bodyMessage, email, userId);
    }


}
