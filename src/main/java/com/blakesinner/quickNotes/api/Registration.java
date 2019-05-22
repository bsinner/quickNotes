package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.ActivationToken;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.ActivationTokenDAO;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import com.blakesinner.quickNotes.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static javax.mail.Message.RecipientType.TO;

@Path("/register")
public class Registration {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final GenericDAO<User> dao = new GenericDAO<>(User.class);

    @GET // TODO: make POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendRegistration(@QueryParam("user") String username
            , @QueryParam("pass") String password
            , @QueryParam("email") String email) {

        if (username == null || password == null || email == null) {
            return Response.status(400).build();
        }

        if (!validEmail(email)) {
            return Response.status(422).entity("{\"err\" : \"email\"}").build();
        }

        if (!validUsername(username)) {
            return Response.status(422).entity("{\"err\" : \"username\"}").build();
        }

        return createAccount(email, password, username);


    }

    private Response createAccount(String email, String password, String username) {
        User user = new User(username, password, email);
        int id = dao.insert(user);
        User foundUser = dao.getByPropertyEqual("id", String.valueOf(id)).get(0);

        ActivationTokenDAO tDao = new ActivationTokenDAO();
        ActivationToken token = new ActivationToken(user);
//        UUID uuid =
                String tid  = tDao.insertToken(token);
//        ActivationToken foundToken = tDao.getByPropertyEqual("id", uuid.toString()).get(0);

//        UUID t = foundToken.getId();
        // insert the token into the database
        // ...
//        http://localhost:8080/quickNotes_war/api/register?user=a&pass=b&email=cc

        return Response.status(200).entity(tDao.getByPropertyEqual("id", tid).get(0).getId()).build();
    }

    private boolean validEmail(String email) {
        List<User> users = dao.getByPropertyEqual("email", email);
        return users.size() == 0;
    }

    private boolean validUsername(String username) {
        List<User> users = dao.getByPropertyEqual("username", username);
        return users.size() == 0;
    }

    private boolean sendEmail() {
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
            return true;
        } catch (MessagingException me) {
            logger.trace(me);
        }

        return false;
    }
}
