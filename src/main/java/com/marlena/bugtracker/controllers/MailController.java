package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.models.Mail;
import com.marlena.bugtracker.services.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mails")
@RequiredArgsConstructor
public class MailController {

    final private MailService mailService;

    @GetMapping
    public String showForm() {
        return "email/index";
    }

    @PostMapping
    public String sendMail(@ModelAttribute Mail mail) {
        mailService.sendMail(mail);
        return "email/index";
    }
}
