package com.example.demo;

import org.springframework.mail.SimpleMailMessage;

public interface Email {
    void sendSimpleMessage(String to,
                           String subject,
                           String text);
}
