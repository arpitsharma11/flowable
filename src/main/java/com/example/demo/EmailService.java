package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
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

    public void sendEmailConfirmation(String email,String body,String subject){
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            System.out.println(email);
            helper.setTo(email);
            helper.setText(body);
            helper.setSubject(subject);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        sender.send(message);

    }

    public void sendEmailSanitation(){
        System.out.println("send email sanitation");
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
        System.out.println("send email review");
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
        System.out.println("send email one");
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
    public void sendMailEmployeeManagerHr(JsonNode jsonNode){
        System.out.println("send email employee manager hr " + jsonNode);

            sendEmailConfirmation(jsonNode.get("employeeEmail").asText(),"zetalent","Appraisal cycle started");
            sendEmailConfirmation(jsonNode.get("managerEmail").asText(),jsonNode.get("name").asText(),"Appraisal cycle started");

    }

    public void sendEmailConfirmation(){
        System.out.println("send email confirmation with no parameters");
    }

    public void sendEmailHr(){
        System.out.println("send email hr");
    }
}
