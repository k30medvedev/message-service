package org.solbeg.soft.messageservice.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.solbeg.soft.messageservice.model.Status;
import org.solbeg.soft.messageservice.service.Template;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;

@Data
@NoArgsConstructor
public class MailDtoRequest {

    //email to
    @NotBlank(message = "Email must not be empty")
    @Email
    private String email;

    @NotBlank
    private Template template;

    private String subject;

    private Status status;

    private HashMap<String, Object> mailData;


}
