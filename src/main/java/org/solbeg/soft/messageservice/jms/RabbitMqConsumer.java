package org.solbeg.soft.messageservice.jms;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solbeg.soft.messageservice.domain.MailDtoRequest;
import org.solbeg.soft.messageservice.service.EMailSender;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
@RequiredArgsConstructor
public class RabbitMqConsumer {

    private final EMailSender senderMail;

    Logger logger = LoggerFactory.getLogger(RabbitListener.class);

    @RabbitListener(queues = "mails.send.message-service")
    public void receive(MailDtoRequest mailData) throws MessagingException {
            if (mailData != null) {
            senderMail.sendEmail(mailData);
            logger.debug("order : " + mailData + " was sent successfully");
        }
    }
}
