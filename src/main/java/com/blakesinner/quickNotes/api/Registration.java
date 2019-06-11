package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.ActivationToken;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.ActivationDAO;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import com.blakesinner.quickNotes.util.ActivationEmail;
import com.blakesinner.quickNotes.util.StringDigester;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.UUID;

/**
 * Endpoint for creating new users and sending confirmation emails.
 * If an email or username is taken the response will include JSON containing
 * one of the following error codes to describe the error.
 *
 * 422001: email taken
 * 422002: username taken
 *
 * @author bsinner
 */
@Path("/register")
public class Registration {

    private final GenericDAO<User> DAO = new GenericDAO<>(User.class);

    /**
     * Create the new user and send a confirmation email, if the
     * parameters are valid.
     *
     * @param username the username
     * @param password the password
     * @param email    the email
     * @param uri      the uri info, needed for sending link back to application
     *                 in confirmation email
     * @return         the Ok response or an error response
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendRegistration(
            @QueryParam("user") String username
            , @QueryParam("pass") String password
            , @QueryParam("email") String email
            , @Context UriInfo uri) {

        Response error = findError(username, password, email);
        if(error != null) return error;

        UUID actTokenId = createAccount(email, password, username);

        if (actTokenId == null) {
            return Response.status(500).entity("{}").build();
        }

        ActivationEmail.send(actTokenId.toString(), uri, email);

        return Response.status(200).entity("{}").build();
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
            return Response.status(400).entity("{}").build();
        }

        if (!validEmail(email)) {
            return accountTaken("email taken", "422001");
        }

        if (!validUsername(username)) {
            return accountTaken("username taken", "422002");
        }

        return null;
    }

    /**
     * Create a new user and an activation token to activate the new account.
     *
     * @param email    the email
     * @param password the password
     * @param username the username
     * @return         the activation token, to be included in the email
     */
    private UUID createAccount(String email, String password, String username) {
        String encrypted = StringDigester.encrypt(password);
        if (encrypted == null) return null;

        User user = new User(username, encrypted, email);

        int id = DAO.insert(user);
        User createdUser = DAO.getByPropertyEqual("id", String.valueOf(id)).get(0);

        return createToken(createdUser);
    }

    /**
     * Create an activation token.
     *
     * @param user the user activated by the token
     * @return     the activation token
     */
    private UUID createToken(User user) {
        ActivationDAO tDao = new ActivationDAO();
        ActivationToken token = new ActivationToken(user);

        return tDao.insertWithUUID(token);
    }

    /**
     * Check if an user with a given email already exists.
     *
     * @param email the email to check for
     * @return      true or false depending on if the email is unique or non unique
     */
    private boolean validEmail(String email) {
        List<User> users = DAO.getByPropertyEqual("email", email);
        return users.size() == 0;
    }

    /**
     * Check if an user with the passed in username exists.
     *
     * @param username the username to search for
     * @return         true or false depending on if the username is unique or non unique
     */
    private boolean validUsername(String username) {
        List<User> users = DAO.getByPropertyEqual("username", username);
        return users.size() == 0;
    }

    /**
     * Get a 422 Unprocessable Entity for when an account is already
     * registered under the new user's username or email.
     *
     * @param msg message indicating whether the username or email is taken
     * @return    the error response
     */
    private Response accountTaken(String msg, String code) {
        return Response.status(422).entity(
                "{" +
                    "\"error\" : {" +
                        "\"code\" : \"" + code + "\"" +
                        ", \"desc\" : \"" + msg + "\"" +
                    "}" +
                "}"
        ).build();
    }

}
