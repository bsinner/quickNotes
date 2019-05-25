package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.ActivationToken;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Endpoint for activating accounts with activation tokens.
 *
 * @author bsinner 
 */
@Path("activate")
public class ActivateAccount {

    /**
     * Activate the account linked to the activation token.
     *
     * @param activateToken the activate token
     * @return              the response indicating success of activating account
     */
    @POST
    public Response activate(@QueryParam("t") String activateToken) {

        if (activateToken == null) {
            return Response.status(400).build();
        }

        return activateUser(activateToken);

    }

    /**
     * Search the database for the token and validate the results, then try to
     * activate the token's user account if the token is valid.
     *
     * @param activateToken the activation token
     * @return              an Ok response if the user could be validated, error
     *                      response otherwise
     */
    private Response activateUser(String activateToken) {
        GenericDAO<ActivationToken> tokenDao = new GenericDAO<>(ActivationToken.class);

        List<ActivationToken> tokens = tokenDao.getByPropertyEqual("id", activateToken);

        Response tokenErr = validateToken(tokens);
        if (tokenErr != null) {
            return tokenErr;
        }

        ActivationToken token = tokens.get(0);

        return updateUser(token);
    }

    /**
     * Find if the token exists, then check if the token is expired.
     *
     * @param tokens the activation token
     * @return       an error response if the token is invalid, null
     *               if no error was found
     */
    private Response validateToken(List<ActivationToken> tokens) {
        if (tokens.size() < 1) {
            return Response.status(404).entity("Error 404: Token not Found").build();
        }

        if (isExpired(tokens.get(0))) {
            return Response.status(410).entity("Error 410: Token expired").build();
        }

        return null;
    }

    /**
     * Try to activate the user, if the user can be activated delete the activation token.
     *
     * @param token the activation token
     * @return      an Ok response if the user was activated, error response if the user
     *              could not be updated
     */
    private Response updateUser(ActivationToken token) {
        GenericDAO<User> dao = new GenericDAO<>(User.class);
        User user = token.getUser();
        user.setActivated(true);

        dao.saveOrUpdate(user);

        List<User> updatedUsers = dao
                .getByPropertyEqual("id", String.valueOf(user.getId()));

        if (updatedUsers.size() < 1 || !updatedUsers.get(0).isActivated()) {
            return Response.status(500).entity("Error 500: Could not activate user").build();
        }

        deleteToken(token);

        return Response.ok("Account activated").build();
    }

    /**
     * Find if a token is expired.
     *
     * @param token the activation token
     * @return      true if the token is expired, false if it's valid
     */
    private boolean isExpired(ActivationToken token) { return LocalDateTime.now().isAfter(token.getExpireDate()); }

    /**
     * Delete a token.
     *
     * @param token the token to delete
     */
    private void deleteToken(ActivationToken token) { new GenericDAO<>(ActivationToken.class).delete(token); }

}
