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
        Note note = getNote();

        Response error = getInvalidResponse(user, note);

        if (error != null) {
            return error;
        }

        updateNote(title, contents);
        return Response.status(204).build();
    }

    private Note getNote() {
        return null;
    }

    private User getUser() {
        return null;
    }

    private Response getInvalidResponse(User user, Note note) {
        return null;
    }

    private void updateNote(String title, String contents) {

    }
}
