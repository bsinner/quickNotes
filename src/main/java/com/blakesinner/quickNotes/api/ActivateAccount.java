package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.ActivationToken;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.ActivationDAO;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Endpoint for activating a user. Users must have the UNACTIVATED role in the database
 * and an activation token for the user must be present.
 *
 * @author bsinner
 */
@Path("activate")
@Secured(roles = "UNACTIVATED")
public class ActivateAccount {

    /**
     * Activate the account linked to the activation token.
     *
     * @param activateToken the activation token
     * @return              response showing if the account was activated
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
        List<ActivationToken> tokens = new ActivationDAO()
                .getByPropertyEqual("id", activateToken);

        Response error = validateToken(tokens);

        if (error != null) {
            return error;
        }

        return updateUser(tokens.get(0));
    }

    /**
     * Find if the token exists, then check if the token is expired.
     *
     * @param tokens the activation token
     * @return       error response if the token is invalid, null
     *               if no errors were found
     */
    private Response validateToken(List<ActivationToken> tokens) {
        if (tokens.isEmpty()) {
            return Response.status(404).entity("Error 404: Token not Found").build();
        }

        if (isExpired(tokens.get(0))) {
            return Response.status(410).entity("Error 410: Token expired").build();
        }

        return null;
    }

    /**
     * Try to activate the user.
     *
     * @param token the activation token
     * @return      an Ok response if the user was activated, error response if the user
     *              could not be activated
     */
    private Response updateUser(ActivationToken token) {
        User user = new ActivationDAO().activateUser(token);

        return user == null
                ? Response.status(500).entity("Error 500: Could not activate user").build()
                : Response.status(200).entity("User activated").build();
    }

    /**
     * Find if a token is expired.
     *
     * @param token the activation token
     * @return      true if the token is expired, false if it's valid
     */
    private boolean isExpired(ActivationToken token) { return LocalDateTime.now().isAfter(token.getExpireDate()); }

}
