package org.solbeg.soft.messageservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solbeg.soft.messageservice.domain.MailDto;
import org.solbeg.soft.messageservice.domain.MailDtoRequest;
import org.solbeg.soft.messageservice.model.Mail;
import org.solbeg.soft.messageservice.model.Status;
import org.solbeg.soft.messageservice.repository.MailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EMailService {

    private final MailRepository mailRepository;
    private final EMailMapper mailMapper;

    @Value("${solbeg.mail.name}")
    String mailFrom;

    Logger logger = LoggerFactory.getLogger(EMailService.class);

    public List<MailDto> findAll() {
        List<Mail> mailList = mailRepository.findAll();
        logger.debug("findAll() in EmailService");
        return mailMapper.mailDtoList(mailList);
    }

    public MailDto findById(String id) {
        Mail existMail = mailRepository.findByUUID(id);
        logger.debug("findById() in EmailService");
        return mailMapper.mailToMailDto(existMail);
    }

    public void deleteById(String uuid) {
        mailRepository.deleteByUUID(uuid);
        logger.debug("mail with " + uuid + " deleted");
    }

    public Mail saveInDataBase(MailDtoRequest mailRequest, String htmlMsg) {
        Mail mail = new Mail();
        mail.setMailFrom(mailFrom);
        mail.setMailTo(mailRequest.getEmail());
        mail.setTemplate(mailRequest.getTemplate());
        mail.setBody(htmlMsg);
        mail.setStatus(mailRequest.getStatus());
        mailRepository.save(mail);
        logger.debug(mail + "sent and save in database");
        return mail;
    }

    public void saveInDataBase(Mail mail) {
        mailRepository.save(mail);
        logger.debug(mail.getStatus() + "Status sent and saved in database");
    }

}
