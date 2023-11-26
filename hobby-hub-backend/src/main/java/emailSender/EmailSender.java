/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package emailSender;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Zuucker
 */
@Component
public class EmailSender {

    private static String serviceEmail;
    private static String servicePassword;
    private static String serviceHost;
    private static String servicePort;
    private static String serviceAuth;
    private static String serviceTls;

    public EmailSender() {
    }

    @Autowired
    public EmailSender(@Value("${mail.email}") String email, @Value("${mail.password}") String password,
            @Value("${mail.host}") String host, @Value("${mail.port}") String port,
            @Value("${mail.auth}") String auth, @Value("${mail.tls}") String tls) {

        serviceEmail = email;
        servicePassword = password;
        serviceHost = host;
        servicePort = port;
        serviceAuth = auth;
        serviceTls = tls;
    }

    public void sendEmail(String to, String subject, String messageText) {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", serviceHost);
        prop.put("mail.smtp.port", servicePort);
        prop.put("mail.smtp.auth", serviceAuth);
        prop.put("mail.smtp.starttls.enable", serviceTls);
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(serviceEmail, servicePassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(serviceEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );

            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();

            htmlPart.setContent(messageText, "text/html");

            multipart.addBodyPart(htmlPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            System.out.println(e.getCause());
        }
    }

}
