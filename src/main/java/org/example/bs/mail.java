package org.example.bs;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.PasswordAuthentication;
import java.util.Properties;

public class mail {
    Static s = new Static();
    public void email(String fromMail, String fromPassword, String toMail) {
        try {
            final String user = fromMail, password = fromPassword;
            Properties prop = new Properties();
            prop.setProperty("mail.smtp.host", "smtp.gmail.com");
            prop.setProperty("mail.smtp.port", "465");
            prop.setProperty("mail.smtp.auth", "true");
            prop.setProperty("mail.smtp.ssl.enable", "true");
//            prop.put("mail.debug", "true");

//            prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session sess = Session.getDefaultInstance(prop, new Authenticator() {

                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(user, password);
                }
            });

//            Session sess=Session.getDefaultInstance(prop);

            sess.setDebug(true);

            Message msg = new MimeMessage(sess);

            msg.setFrom(new InternetAddress(fromMail));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
            msg.setSubject(s.subject);
            msg.setText(s.text);
            msg.setContent(s.text, "text/html");


            Transport.send(msg);
            System.out.println("successfully sent");
        } catch (MessagingException msgEx) {
            msgEx.printStackTrace();
        }
    }
}
//        java.util.Properties props = new java.util.Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        Session session = Session.getDefaultInstance(props, null);
//
//// Construct the message
//        String to = "sltanyh1384@gmail.com";
//        String from = "sltanyh468@gmail.com";
//        String subject = "Hello";
//        Message msg = new MimeMessage(session);
//        try {
//            msg.setFrom(new InternetAddress(from));
//            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
//            msg.setSubject(subject);
//            msg.setText("Hi,\n\nHow are you?");
//
//            // Send the message.
//            Transport.send(msg);
//            System.out.println("sende");
//        } catch (MessagingException e) {
//           e.printStackTrace();
//        }

//        String recipient = "sltanyh1384@gmail.com";
//
//        String sender = "sltanyh468@gmail.com";
//
//        String host = "smtp.gmail.com";
//
//        Properties properties = System.getProperties();
//
//        properties.setProperty("mail.smtp.host", host);
//
//        Session session = Session.getDefaultInstance(properties);
//
//        try
//        {
//            MimeMessage message = new MimeMessage(session);
//
//            message.setFrom(new InternetAddress(sender));
//
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
//
//            message.setSubject("This is Subject");
//
//            message.setText("This is a test mail");
//
//            Transport.send(message);
//            System.out.println("Mail successfully sent");
//        }
//        catch (MessagingException mex)
//        {
//            mex.printStackTrace();
//        }