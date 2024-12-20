package br.com.rafaelvieira.cryptocypher.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    private final Session session;

    public EmailService() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.example.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        this.session = Session.getInstance(props);
    }

    public void sendVerificationEmail(String to, String code) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("your_email@example.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Email Verification");
        message.setText("Your verification code is: " + code);

        Transport.send(message);
    }

    public void sendPasswordResetEmail(String to, String code) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("your_email@example.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Password Reset");
        message.setText("Your password reset code is: " + code);

        Transport.send(message);
    }
}

//package br.com.rafaelvieira.cryptocypher.service;
//
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//    private final JavaMailSender mailSender;
//
//    public EmailService(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    public void sendVerificationEmail(String to, String code) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject("Email Verification");
//        message.setText("Your verification code is: " + code);
//        mailSender.send(message);
//    }
//
//    public void sendPasswordResetEmail(String to, String code) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject("Password Reset");
//        message.setText("Your password reset code is: " + code);
//        mailSender.send(message);
//    }
//}
