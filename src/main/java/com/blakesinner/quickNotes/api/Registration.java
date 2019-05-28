package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.ActivationToken;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.ActivationTokenDAO;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import com.blakesinner.quickNotes.util.CookieUtil;
import com.blakesinner.quickNotes.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Properties;
import static javax.mail.Message.RecipientType.TO;

/**
 * Endpoint for creating new users and sending confirmation emails.
 *
 * @author bsinner
 */
@Path("/register")
public class Registration {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final GenericDAO<User> dao = new GenericDAO<>(User.class);

    /**
     * Create the new user and send a confirmation email, if the
     * parameters are valid.
     *
     * @param username the username
     * @param password the password
     * @param email    the email
     * @return         the Ok response or an error response
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendRegistration(
            @QueryParam("user") String username
            , @QueryParam("pass") String password
            , @QueryParam("email") String email) {

        Response error = findError(username, password, email);
        if(error != null) return error;

        String activationToken = createAccount(email, password, username);
        sendEmail(activationToken);

        return okResponse();
    }

    /**
     * Create a new activation token and send it to the users email, user must be logged in
     * to use this endpoint.
     *
     * @param cookie the user access token
     * @return       the Ok response or error response
     */
    @PUT
    @Path("/resend")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resendRegistration(@CookieParam("access_token") Cookie cookie) {
        User user = findValidUser(cookie);

        if (user == null) {
            return badRequest();
        } else if (user.isActivated()) {
            return Response.status(403).entity("{}").build();
        }

        sendEmail(createToken(user));

        return okResponse();
    }

    /**
     * Check that the required parameters where included, and check if a
     * user with the passed in username or email already exists.
     *
     * @param username the username
     * @param password the password
     * @param email    the email
     * @return         the error response, or null if no errors where found
     */
    private Response findError(String username, String password, String email) {

        if (username == null || password == null || email == null) {
            return badRequest();
        }

        if (!validEmail(email)) {
            return accountTaken("email");
        }

        if (!validUsername(username)) {
            return accountTaken("username");
        }

        return null;
    }

    /**
     * Try to find the user in the access token in the database.
     *
     * @param cookie the user access cookie
     * @return       the found user, or null if no user could be found
     */
    private User findValidUser(Cookie cookie) {
        if (cookie != null) {
            String id = CookieUtil.jaxRsGetId(cookie);

            if (id != null) {
                List<User> users = dao.getByPropertyEqual("id", id);

                if (users.size() > 0) {
                    return users.get(0);
                }
            }
        }

        return null;
    }

    /**
     * Create a new user and an activation token to activate the new account.
     *
     * @param email    the email
     * @param password the password
     * @param username the username
     * @return         the activation token ID, to be included in the email
     */
    private String createAccount(String email, String password, String username) {
        User user = new User(username, password, email);
        int id = dao.insert(user);
        User createdUser = dao.getByPropertyEqual("id", String.valueOf(id)).get(0);

        return createToken(createdUser);
    }

    /**
     * Create an activation token.
     *
     * @param user the user activated by the token
     * @return     the token ID
     */
    private String createToken(User user) {
        ActivationTokenDAO tDao = new ActivationTokenDAO();
        ActivationToken token = new ActivationToken(user);

        return tDao.insertToken(token);
    }

    /**
     * Send an email with an account activation link.
     *
     * @param token the activation token ID
     */
    private void sendEmail(String token) {
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
            message.setContent(getHtml(token), "text/html");

            Transport.send(message);
        } catch (MessagingException me) {
            logger.trace(me);
        }
    }

    /**
     * Check if an user with a given email already exists.
     *
     * @param email the email to check for
     * @return      true or false depending on if the email is unique or non unique
     */
    private boolean validEmail(String email) {
        List<User> users = dao.getByPropertyEqual("email", email);
        return users.size() == 0;
    }

    /**
     * Check if an user with the passed in username exists.
     *
     * @param username the username to search for
     * @return         true or false depending on if the username is unique or non unique
     */
    private boolean validUsername(String username) {
        List<User> users = dao.getByPropertyEqual("username", username);
        return users.size() == 0;
    }

    /**
     * Get the HTML to send in the confirmation email.
     *
     * @param token the activation token ID
     * @return      the email HTML
     */
    private String getHtml(String token) {
        return "<div style=\"margin: 1em, 1em, 1em, 1em;\">"
                + "<h3 style=\"font-family:Lato,'Helvetica Neue',Arial,Helvetica,sans-serif\">"
                    + "Account Created"
                + "</h3>"
                + "<h5 style=\"font-family:Lato,'Helvetica Neue',Arial,Helvetica,sans-serif;\">"
                    + "Your Quick Notes account has been created, click here to activate."
                + "</h5>"
                + "<a class=\"ui button\" href=\"" + token + "\""
                        + "style=8\"cursor: pointer;display: inline-block;min-height: 1em;outline: 0;border: none;vertical-align: baseline;background: #e0e1e2 none;color: rgba(0,0,0,.6);font-family: Lato,'Helvetica Neue',Arial,Helvetica,sans-serif;margin: 0 .25em 0 0;padding: .78571429em 1.5em .78571429em;font-weight: 700;line-height: 1em;font-style: normal;text-align: center;border-radius: .28571429rem;margin-left: auto; margin-right: auto;\">"
                    + "Confirm Account"
                + "</a>"
                + " </div>";
    }

    /**
     * Get an Error 400 Bad Request response.
     *
     * @return the error response
     */
    private Response badRequest() { return Response.status(400).entity("{}").build(); }

    /**
     * Get a 200 Ok response.
     *
     * @return the Ok response
     */
    private Response okResponse() { return Response.status(200).entity("{}").build(); }

    /**
     * Get a 422 Unprocessable Entity response for when an account is
     * already registered under the new user's username or email.
     *
     * @param msg message indicating whether the username or email is taken
     * @return    the error response
     */
    private Response accountTaken(String msg) {
        return Response.status(422).entity("{\"err\" : \"" + msg + "\"}").build();
    }
}
