package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
//import java.util.Calendar;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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

    public String sendInvitation(JsonNode jsonNode) throws MessagingException {


//        System.out.println("send invitation service called");
//         String version =    "VERSION:2.0\r\n";
//         String prodid =     "PRODID://Elara/lofy/tanare/delp/314sum2015//\r\n";
//         String calBegin =   "BEGIN:VCALENDAR\r\n";
//         String calEnd =     "END:VCALENDAR\r\n";
//         String eventBegin = "BEGIN:VEVENT\r\n";
//         String eventEnd =   "END:VEVENT\r\n";
//
//            StringBuilder builder = new StringBuilder();
//            builder.append("invite");
//            builder.append(".ics");
//
//            String testExample = "UID:uid1@example.com\r\nDTSTAMP:19970714T170000Z\r\nORGANIZER;CN=John Doe:MAILTO:apoorjayanth.kumar@zemosolabs.com\r\nDTSTART:19970714T170000Z\r\nDTEND:19970715T035959Z\r\nSUMMARY:Bastille Day Party\r\n";
//
//            try {
//
//                File file = new File(builder.toString());
//
//                // if file doesnt exists, then create it
//                if (!file.exists()) {
//                    file.createNewFile();
//                }
//
//                FileWriter fw = new FileWriter(file.getAbsoluteFile());
//                BufferedWriter bw = new BufferedWriter(fw);
//                bw.write(calBegin);
//                bw.write(version);
//                bw.write(prodid);
//                bw.write(eventBegin);
//                bw.write(testExample);
//                bw.write(eventEnd);
//                bw.write(calEnd);
//
//                bw.close();
//
//                System.out.println("Done");
//
//                //File file = new File("src/test/resources/validation.txt");
////                DiskFileItem fileItem = new DiskFileItem("file", "text/plain", false, file.getName(), (int) file.length() , file.getParentFile());
////                fileItem.getOutputStream();
////                MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
//
//
//                FileInputStream input = new FileInputStream(file);
//                MultipartFile multipartFile = new MockMultipartFile("invite.ics",
//                        file.getName(), "application/ics", IOUtils.toByteArray(input));
//                helper.addAttachment("invite.ics",multipartFile);
//
//                Multipart multipart = helper.getMimeMultipart();
//                multipart.
//            } catch (IOException e) {
//                e.printStackTrace();
//            }



        try {
            String from = "FromExample@gmail.com";
            String to = "apoorjayanth.kumar@zemosolabs.com";
//            Properties prop = new Properties();
//
//            prop.put("mail.smtp.auth", "true");
//            prop.put("mail.smtp.starttls.enable", "true");
//            prop.put("mail.smtp.host", "smtp.gmail.com");
//            prop.put("mail.smtp.port", "587");

//            final String username = "FromExample@gmail.com";
//            final String password = "123456789";

//            Session session = Session.getInstance(prop,
//                    new javax.mail.Authenticator() {
//                        protected PasswordAuthentication getPasswordAuthentication() {
//                            return new PasswordAuthentication(username, password);
//                        }
//                    });

            // Define message
            //MimeMessage message = new MimeMessage(session);
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo("apoorjayanth.kumar@zemosolabs.com");
            message.addHeaderLine("method=REQUEST");
            message.addHeaderLine("charset=UTF-8");
            message.addHeaderLine("component=VEVENT");

            //message.setFrom(new InternetAddress(from));
            //message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            //message.setSubject("Calendar invitation");

            StringBuffer sb = new StringBuffer();

            InputStream is = new FileInputStream("/home/zemoso/Desktop/flowable/src/main/resources/invite.ics");
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            //StringBuilder sb = new StringBuilder();
            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }




//            StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n" +
//                    "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n" +
//                    "VERSION:2.0\n" +
//                    "METHOD:REQUEST\n" +
//                    "BEGIN:VEVENT\n" +
//                    "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:divysaxena2@gmail.com\n" +
//                    "ORGANIZER:MAILTO:apoorjayanth@gmail.com\n" +
//                    "DTSTART:20180922T053000Z\n" +
//                    "DTEND:20180927T060000Z\n" +
//                    "LOCATION:Conference room\n" +
//                    "TRANSP:OPAQUE\n" +
//                    "SEQUENCE:0\n" +
//                    "UID:040000008200E00074C5B7101A82E00800000000002FF466CE3AC5010000000000000000100\n" +
//                    " 000004377FE5C37984842BF9440448399EB02\n" +
//                    "DTSTAMP:20180922T120102Z\n" +
//                    "CATEGORIES:Meeting\n" +
//                    "DESCRIPTION:This the description of the meeting.\n\n" +
//                    "SUMMARY:Zetalent demo\n" +
//                    "PRIORITY:5\n" +
//                    "CLASS:PUBLIC\n" +
//                    "BEGIN:VALARM\n" +
//                    "TRIGGER:PT1440M\n" +
//                    "ACTION:DISPLAY\n" +
//                    "DESCRIPTION:Reminder\n" +
//                    "END:VALARM\n" +
//                    "END:VEVENT\n" +
//                    "END:VCALENDAR");

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

            // send message
            sender.send(message);

            System.out.println("Success");

        } catch (MessagingException me) {
            me.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "Success";
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


