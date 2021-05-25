package org.solbeg.soft.messageservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solbeg.soft.messageservice.domain.MailDtoRequest;
import org.solbeg.soft.messageservice.model.Mail;
import org.solbeg.soft.messageservice.model.Status;
import org.solbeg.soft.messageservice.util.Password;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class EMailSender {

    Logger logger = LoggerFactory.getLogger(EMailSender.class);

    private final JavaMailSenderImpl javaMailSender;
    private final EMailService mailService;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Value("${solbeg.mail.name}")
    String mailFrom;

    public void sendEmail(MailDtoRequest mailRequest) throws MessagingException {
        logger.debug("sendEmail get: " + mailRequest);

        // prepare message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

        // set mailFrom, set mailTo, set subject
        message.setFrom(mailFrom);
        message.setTo(mailRequest.getEmail());
        message.setSubject(mailRequest.getSubject());

        // create context
        Context context = new Context();
        context.setVariables(mailRequest.getMailData());

        // get html from template to send consumer
        String htmlMsg = getHtmlFromContext(context, mailRequest.getTemplate());

        message.setText(htmlMsg, true);

        // save message in Db
        mailRequest.setStatus(Status.NEW);
        Mail mailToDb = mailService.saveInDataBase(mailRequest, htmlMsg);
        //send message
        try {
            javaMailSender.send(mimeMessage);
            // save message in Db
            mailToDb.setStatus(Status.SENT);
            mailService.saveInDataBase(mailToDb);
        } catch (MailException ex) {
            logger.debug(ex.getMessage());
            mailToDb.setStatus(Status.ERROR);
            // save message in Db with error message
            mailToDb.setError("Message didn't send");
            mailService.saveInDataBase(mailToDb);
        }
    }

    private String getHtmlFromContext(Context context, Template template) {
        switch (template) {
            case SEND_ORDER:
                return thymeleafTemplateEngine.process("db-send-order", context);

            case FORGET_PASSWORD:
                context.setVariable("password", Password.getInstance());
                return thymeleafTemplateEngine.process("db-forget-password", context);

            case SEND_NOTIFICATION:
                return thymeleafTemplateEngine.process("db-send-notification", context);
        }
        return thymeleafTemplateEngine.process("db-send-notification", context);

    }

}




