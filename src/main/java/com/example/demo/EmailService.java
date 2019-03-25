package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.parameter.Rsvp;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletOutputStream;
import java.io.*;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

import static net.fortuna.ical4j.model.Property.METHOD;
import static net.fortuna.ical4j.model.Property.ORGANIZER;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender sender;

    @Autowired
    private CalendarQuickstart quickstart;

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
    public void sendEmailManagerEmployeeHr(JsonNode jsonNode){
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


    public String invitation(JsonNode jsonNode) throws ValidationException, URISyntaxException, MessagingException, IOException {
        String managerEmail = jsonNode.get("managerEmail").asText();
        String employeeEmail = jsonNode.get("employeeEmail").asText();
        String description = jsonNode.get("description").asText();
        calendarInvitation(managerEmail,employeeEmail,description);
        //calendarInvitation(employeeEmail,description);

        return "sent successfully";
    }


    public String calendarInvitation(String managerEmail, String employeeEmail, String description) throws IOException, ValidationException, MessagingException, URISyntaxException {

        String[] emails = new String[2];
        emails[0] = managerEmail;
        emails[1] = employeeEmail;
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        message.addHeaderLine("method=REQUEST");
        message.addHeaderLine("charset=UTF-8");
        message.addHeaderLine("component=VEVENT");

        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        calendar.getProperties().add(Method.REQUEST);

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
        cal.set(java.util.Calendar.DAY_OF_MONTH, 26);
        // initialise as an all-day event..
        VEvent vEvent = new VEvent(new Date(cal.getTime()), new Date(cal.getTime()),"One-on-one");
        vEvent.getProperties().add(new Description(description));
        //vEvent.getProperties().add(new Rsvp(true));
        vEvent.getProperties().add(new Location("jupiter"));
        Attendee dev1 = new Attendee(URI.create(emails[0]));
        dev1.getParameters().add(Role.REQ_PARTICIPANT);
        dev1.getParameters().add(Rsvp.TRUE);
        dev1.getParameters().add(PartStat.NEEDS_ACTION);
        vEvent.getProperties().add(dev1);

        Organizer  organizer = new Organizer(emails[1]);
        vEvent.getProperties().add(organizer);
        vEvent.getProperties().add(Status.VEVENT_CONFIRMED);
        vEvent.getProperties().add(Transp.OPAQUE);

        // Generate a UID for the event..
        UidGenerator ug = new UidGenerator("1");
        vEvent.getProperties().add(ug.generateUid());
        calendar.getComponents().add(vEvent);

//        ServletOutputStream fout = response.getOutputStream();
//        CalendarOutputter outputter = new CalendarOutputter();
//        outputter.output(calendar, fout);

        FileOutputStream fout = new FileOutputStream("mycalendar.ics");

        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar, fout);

        StringBuffer sb = new StringBuffer();

        InputStream is = new FileInputStream("mycalendar.ics");
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        //StringBuilder sb = new StringBuilder();
        while(line != null){
            sb.append(line).append("\n");
            line = buf.readLine();
        }
        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();

        // Fill the message
        messageBodyPart.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
        messageBodyPart.setHeader("Content-ID", "calendar_message");
        messageBodyPart.setDataHandler(new DataHandler(
                new ByteArrayDataSource(sb.toString(), "text/calendar")));// very important

        // Create a Multipart
        Multipart multipart = new MimeMultipart();

        // Add part one
        multipart.addBodyPart(messageBodyPart);

        // Put parts in message
        message.setContent(multipart);
        helper.setSubject("Invitation");

        helper.setTo(emails);
        sender.send(message);

        return "success";
    }

    public String sendInvitationEmailManagerEmployee(){
        System.out.println("send invitation email manager employee");
        return "Send invitation email manager employee";
    }
}


