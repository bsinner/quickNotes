package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Properties;

import static javax.mail.Message.RecipientType.TO;

@Path("/register")
public class Registration {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @GET // TODO: make POST
    public String sendRegistration() {
        PropertiesLoader loader = new PropertiesLoader();
        Properties mailProps = loader.load("/mail.properties");
        Properties authProps = loader.load("/mailLogin.properties");

        Session session = Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        authProps.getProperty("user")
                        , authProps.getProperty("pass"));
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(authProps.getProperty("user")));
            message.setRecipient(TO, new InternetAddress(authProps.getProperty("user")));
            message.setSubject("Confirm Account");
            message.setContent(
                "<div style=\"margin: 1em, 1em, 1em, 1em;\">"
                    + "<h3 style=\"font-family:Lato,'Helvetica Neue',Arial,Helvetica,sans-serif\">"
                        + "Account Created"
                    + "</h3>"
                    + "<h5 style=\"font-family:Lato,'Helvetica Neue',Arial,Helvetica,sans-serif;\">"
                        + "Your Quick Notes account has been created, click here to activate."
                    + "</h5>"
                    + "<a class=\"ui button\" style=\"cursor: pointer;display: inline-block;min-height: 1em;outline: 0;border: none;vertical-align: baseline;background: #e0e1e2 none;color: rgba(0,0,0,.6);font-family: Lato,'Helvetica Neue',Arial,Helvetica,sans-serif;margin: 0 .25em 0 0;padding: .78571429em 1.5em .78571429em;font-weight: 700;line-height: 1em;font-style: normal;text-align: center;border-radius: .28571429rem;margin-left: auto; margin-right: auto;\">"
                        + "Confirm Account"
                    + "</a>"
                + " </div>"
            , "text/html");

            Transport.send(message);
        } catch (MessagingException me) {
            logger.trace(me);
        }

        return "...";
    }

}
