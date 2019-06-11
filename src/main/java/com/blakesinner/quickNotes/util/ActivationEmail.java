package com.blakesinner.quickNotes.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.UriInfo;
import java.util.Properties;
import static javax.mail.Message.RecipientType.TO;

/**
 * Utility class for sending account activation emails.
 *
 * @author bsinner
 */
public class ActivationEmail {

    private static final Logger LOGGER = LogManager.getLogger(ActivationEmail.class);
    private static final String BASE_PATH = "/api/";

    /**
     * Send the activation email.
     *
     * @param token the activation token id
     * @param uri   the uri information, used to generate link
     * @param dest  the recipient
     */
    public static void send(String token, UriInfo uri, String dest) {
        PropertiesLoader loader = new PropertiesLoader();
        Properties mailProps = loader.load("/mail.properties");
        Properties authProps = loader.load("/mailLogin.properties");

        Session mailSession = Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        authProps.getProperty("user"), authProps.getProperty("pass")
                );
            }
        });

        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(authProps.getProperty("user")));
            message.setRecipient(TO, new InternetAddress(dest));
            message.setSubject("Confirm Account");
            message.setContent(getBody(token, uri), "text/html");

            Transport.send(message);
        } catch (MessagingException me) {
            LOGGER.trace(me);
        }
    }

    /**
     * Get the email body.
     *
     * @param token the activation token
     * @param uri   the uri information
     * @return      the body text
     */
    private static String getBody(String token, UriInfo uri) {
        String url = uri.getBaseUri().toString().replace(BASE_PATH, "")
                + "activate?t=" + token;

        return "<div style=\"margin: 1em, 1em, 1em, 1em;\">"
                + "<h3 style=\"font-family:Lato,'Helvetica Neue',Arial,Helvetica,sans-serif\">"
                    + "Account Created&nbsp;"
                + "</h3>"
                + "<h5 style=\"font-family:Lato,'Helvetica Neue',Arial,Helvetica,sans-serif;\">"
                    + "Your Quick Notes account has been created, click here to activate."
                + "</h5>"
                + "<a class=\"ui button\" href=\"" + url + "\""
                        + "style=8\"cursor: pointer;display: inline-block;min-height: 1em;outline: 0;border: none;vertical-align: baseline;background: #e0e1e2 none;color: rgba(0,0,0,.6);font-family: Lato,'Helvetica Neue',Arial,Helvetica,sans-serif;margin: 0 .25em 0 0;padding: .78571429em 1.5em .78571429em;font-weight: 700;line-height: 1em;font-style: normal;text-align: center;border-radius: .28571429rem;margin-left: auto; margin-right: auto;\">"
                    + "Confirm Account"
                + "</a>"
                + " </div>";
    }
}
