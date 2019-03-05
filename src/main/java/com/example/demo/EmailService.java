package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender sender;

    public void sendEmailConfirmation(){
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo("arpit.sharma@zemosolabs.com");
            helper.setText("Zetalent");
            helper.setSubject("Appraisal Cycle Stared");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        sender.send(message);

    }

    public void sendEmailSanitation(){
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo("arpit.sharma@zemosolabs.com");
            helper.setText("Zetalent");
            helper.setSubject("Sanitation1");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        sender.send(message);

    }

    public void sendEmailReview(){
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo("arpit.sharma@zemosolabs.com");
            helper.setText("Zetalent");
            helper.setSubject("Review done");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        sender.send(message);

    }
    public void sendEmailOne(){
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo("arpit.sharma@zemosolabs.com");
            helper.setText("Zetalent");
            helper.setSubject("One on one");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        sender.send(message);

    }
    public void sendEmailHr(){
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo("arpit.sharma@zemosolabs.com");
            helper.setText("Zetalent");
            helper.setSubject("Done");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        sender.send(message);

    }
}
