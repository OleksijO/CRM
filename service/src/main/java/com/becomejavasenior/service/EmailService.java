package com.becomejavasenior.service;

import org.springframework.mail.SimpleMailMessage;

import java.util.List;

public interface EmailService {

    void sendEmail(SimpleMailMessage email);

    void sendEmails (List<SimpleMailMessage> emails);
}
