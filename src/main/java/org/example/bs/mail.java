package org.example.bs;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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

            Session sess = Session.getDefaultInstance(prop, new Authenticator() {

                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(user, password);
                }
            });

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
