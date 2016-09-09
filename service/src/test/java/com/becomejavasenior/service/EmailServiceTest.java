package com.becomejavasenior.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class EmailServiceTest extends SpringServiceTests{
    @Autowired
    private MailSender mailSender;

    @Test
    public void testSendEmail(){

        SimpleMailMessage email=new SimpleMailMessage();
        email.setSubject("THIS IS TEST MESSAGE");
        email.setText("This is test text in email's body.");
        email.setTo("oleksij.onysymchuk@gmail.com");
        email.setFrom("oleksij.onysymchuk@gmail.com");
        mailSender.send(email);

    }
}
