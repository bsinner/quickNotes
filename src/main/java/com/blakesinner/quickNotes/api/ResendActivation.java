package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.ActivationToken;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.ActivationDAO;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import sun.rmi.server.Activation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/register/resend")
@Secured(roles = "UNACTIVATED")
public class ResendActivation {

    @GET // TODO: PUT
    public Response resendActivationEmail(@Context SecurityContext context, @Context UriInfo uri) {
        User user = getUser(context);
        if (user == null) {
            return serverError();
        }

        ActivationToken token = createToken(user);
        if (token == null) {
            return serverError();
        }

//        sendEmail(token, uri, "quicknotesmail@gmail.com")

        return Response.status(200).entity("Email Sent").build();
    }

    private User getUser(SecurityContext context) {
        List<User> results = new GenericDAO<>(User.class).getByPropertyEqual("id"
                , context.getUserPrincipal().getName());

        return results.isEmpty() ? null : results.get(0);
    }

    private ActivationToken createToken(User user) {
        ActivationToken token = new ActivationToken(user);
        ActivationDAO dao = new ActivationDAO();

        String id = dao.insertToken(token);

        List<ActivationToken> results = dao.getByPropertyEqual("id", id);

        return results.isEmpty() ? null : results.get(0);
    }

    private Response serverError() {
        return Response.status(500)
            .entity("Error 500: Could not issue activation token").build();
    }
}
