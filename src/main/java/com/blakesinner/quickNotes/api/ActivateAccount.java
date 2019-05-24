package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.ActivationToken;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("activate")
public class ActivateAccount {

    @GET //TODO: post?
    public Response activate(
            @QueryParam("t") String activateToken
//            , @QueryParam("id") String id
            , @CookieParam("access_token") Cookie cookie) {

        if (activateToken == null) {
            return Response.status(400).build();
        }

        return activateUser(activateToken);
    }

    private Response activateUser(String activateToken) {
        GenericDAO<User> userDao = new GenericDAO<>(User.class);
        GenericDAO<ActivationToken> tokenDao = new GenericDAO<>(ActivationToken.class);

        List<ActivationToken> tokens = tokenDao.getByPropertyEqual("id", activateToken);

        if (tokens.size() < 1) {
            return Response.status(404).entity("Token not Found").build();
        }
        return null;
    }
}
