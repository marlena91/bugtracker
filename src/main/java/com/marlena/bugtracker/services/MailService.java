package com.marlena.bugtracker.services;

import com.marlena.bugtracker.models.Mail;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    final private JavaMailSender javaMailSender;

    public void sendMail(Mail mail) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setTo(mail.getRecipient());
            mimeMessageHelper.setSubject("DONE "+mail.getSubject());
            mimeMessageHelper.setText("Your issue has changed its status to DONE");

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            System.out.println("The message has not been sent");
        }
    }
}
