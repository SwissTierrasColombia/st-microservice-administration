package com.ai.st.microservice.administration.business;

import com.ai.st.microservice.administration.services.tracing.SCMTracing;
import com.ai.st.microservice.common.clients.NotifierFeignClient;
import com.ai.st.microservice.common.dto.notifier.MicroserviceNotificationRecoverAccountDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationBusiness {

    private final Logger log = LoggerFactory.getLogger(NotificationBusiness.class);

    private final NotifierFeignClient notifierClient;

    public NotificationBusiness(NotifierFeignClient notifierClient) {
        this.notifierClient = notifierClient;
    }

    public void sendNotificationRecoverAccount(String email, String code, String username, String expirationDate,
            Long userCode) {

        try {

            MicroserviceNotificationRecoverAccountDto notification = new MicroserviceNotificationRecoverAccountDto();
            notification.setEmail(email);
            notification.setCode(code);
            notification.setUsername(username);
            notification.setExpirationDate(expirationDate);
            notification.setStatus(0);
            notification.setType("recoverAccount");
            notification.setUserCode(userCode);

            notifierClient.recoverAccount(notification);

        } catch (Exception e) {
            String messageError = String.format(
                    "Error enviando la notificación de recuperación de cuenta al usuario %d : %s", userCode,
                    e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
        }

    }

}
