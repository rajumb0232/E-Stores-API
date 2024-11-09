package com.devb.estores.serviceimpl;

import com.devb.estores.dto.MessageData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@AllArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void sendMail(MessageData messageData) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(messageData.getTo());
        helper.setSubject(messageData.getSubject());
        helper.setSentDate(messageData.getSentDate());
        helper.setText(messageData.getText(), true);
        javaMailSender.send(message);
    }

    public String createHtmlFor(String template, Map<String, Object> variables) {
        Context context =  new Context();
        context.setVariables(variables);
        return templateEngine.process(template, context);
    }
}
