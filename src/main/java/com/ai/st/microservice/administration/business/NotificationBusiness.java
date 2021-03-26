package com.ai.st.microservice.administration.business;

import com.ai.st.microservice.common.clients.NotifierFeignClient;
import com.ai.st.microservice.common.dto.notifier.MicroserviceNotificationRecoverAccountDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationBusiness {

    private final Logger log = LoggerFactory.getLogger(NotificationBusiness.class);

    @Autowired
    private NotifierFeignClient notifierClient;

    public void sendNotificationRecoverAccount(String email, String code, String expirationDate, Long userCode) {

        try {

            MicroserviceNotificationRecoverAccountDto notification = new MicroserviceNotificationRecoverAccountDto();
            notification.setEmail(email);
            notification.setCode(code);
            notification.setExpirationDate(expirationDate);
            notification.setStatus(0);
            notification.setType("success");
            notification.setUserCode(userCode);

            notifierClient.recoverAccount(notification);

        } catch (Exception e) {
            log.error("Error enviando la notificación #1: " + e.getMessage());
        }

    }

}
