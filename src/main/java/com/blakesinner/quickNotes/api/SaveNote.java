package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.Note;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/saveNote")
public class SaveNote {

    @Context
    private SecurityContext securityContext;
    private final static GenericDAO<Note> dao = new GenericDAO<>(Note.class);

    @GET //TODO: change to post
    public Response saveNote(
            @QueryParam("id") String id
            , @QueryParam("title") String title
            , @QueryParam("contents") String contents) {

        User user = getUser();
        Note note = getNote(id);

        Response error = getInvalidResponse(user, note);

        if (error != null) {
            return error;
        }

        updateNote(title, contents);
        return Response.status(204).build();
    }

    private Response getInvalidResponse(User user, Note note) {
        return null;
    }

    private void updateNote(String title, String contents) {

    }


    private Note getNote(String id) {
        List<Note> notes = dao.getByPropertyEqual("id", id);
        if (notes.size() > 0) return notes.get(0);
        return null;
    }

    private User getUser() {
        return new GenericDAO<>(User.class)
                .getByPropertyEqual(
                        "id"
                        , securityContext.getUserPrincipal().getName())
                .get(0);
    }

}
