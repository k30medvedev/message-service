package org.solbeg.soft.messageservice.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solbeg.soft.messageservice.domain.MailDto;
import org.solbeg.soft.messageservice.domain.MailDtoRequest;
import org.solbeg.soft.messageservice.service.EMailSender;
import org.solbeg.soft.messageservice.service.EMailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class MailController {

    private final EMailService emailService;
    private final EMailSender eMailSender;

    Logger logger = LoggerFactory.getLogger(MailController.class);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/mails")
    ResponseEntity<List<MailDto>> getAllMails() {
        logger.debug("getAllMails() from MailController was requested");
        return new ResponseEntity<>(emailService.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/mails/{id}")
    ResponseEntity<MailDto> findMailById(@PathVariable String id) {
        MailDto mailDto = emailService.findById(id);
        logger.debug("mail with: " + id + "was requested");
        return ResponseEntity.status(200)
                .body(mailDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/mails/{id}")
    public ResponseEntity<String> deleteMailById(@PathVariable("id") String id) {
        emailService.deleteById(id);
        logger.debug("mail with: " + id + "was requested to delete");
        return ResponseEntity.ok("mail was deleted:" + id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/mails")
    public ResponseEntity<String> sendEmail(@RequestBody MailDtoRequest mailRequest) throws MessagingException {
        eMailSender.sendEmail(mailRequest);
        logger.debug("new mail requested to create:" + mailRequest);
        return ResponseEntity.status(201).body("Email sent");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/")
    String getMainPage() {
        return "Main page";
    }
}
