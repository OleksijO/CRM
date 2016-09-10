package com.becomejavasenior.service.impl;

import com.becomejavasenior.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("emailService")
public class EmailServiceImpl implements EmailService {
    @Autowired
    private MailSender mailSender;

    @Override
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send();
    }

    @Override
    public void sendEmails(List<SimpleMailMessage> emails) {
        mailSender.send((SimpleMailMessage[]) emails.stream().toArray());
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
}
