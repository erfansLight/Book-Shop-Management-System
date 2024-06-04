//package org.example.bs;
//
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.net.PasswordAuthentication;
//import java.util.Properties;
//
//public class mail {
//    public void email() {
//        String recipient = "recipient@gmail.com";
//
//        String sender = "sltanyh468@gmail.com";
//
//        String host = "192.168.11.30";
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
//    }
//}