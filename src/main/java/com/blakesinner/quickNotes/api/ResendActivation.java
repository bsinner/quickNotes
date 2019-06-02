package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.ActivationToken;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.ActivationDAO;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import com.blakesinner.quickNotes.util.ActivationEmail;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Create a new activation token and email a link to activate it to the user.
 *
 * @author bsinner
 */
@Path("/register/resend")
@Secured(roles = "UNACTIVATED")
public class ResendActivation {

    /**
     * Resend activation email.
     *
     * @param context the security context, needed to identify the user
     * @param uri     the uri information, needed because the link back to the
     *                application sent in the email changes between localhost and AWS
     * @return        the response
     */
    @PUT
    public Response resendActivationEmail(@Context SecurityContext context, @Context UriInfo uri) {
        User user = getUser(context);
        if (user == null) {
            return serverError();
        }

        ActivationToken token = createToken(user);
        if (token == null) {
            return serverError();
        }

        ActivationEmail.send(token.getId(), uri, user.getEmail());

        return Response.status(200).entity("Email Sent").build();
    }

    /**
     * Get the current user.
     *
     * @param context the secuirty context containing the user id
     * @return        the current user, or null if no user could be found
     */
    private User getUser(SecurityContext context) {
        List<User> results = new GenericDAO<>(User.class).getByPropertyEqual("id"
                , context.getUserPrincipal().getName());

        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Create an activation token.
     *
     * @param user the user activated by the token
     * @return     the activation token, or null if an error occurred
     */
    private ActivationToken createToken(User user) {
        ActivationToken token = new ActivationToken(user);
        ActivationDAO dao = new ActivationDAO();

        String id = dao.insertToken(token);

        List<ActivationToken> results = dao.getByPropertyEqual("id", id);

        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Get server error response.
     *
     * @return the server error response.
     */
    private Response serverError() {
        return Response.status(500)
            .entity("Error 500: Could not issue activation token").build();
    }
}
