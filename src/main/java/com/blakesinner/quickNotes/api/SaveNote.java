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

@Secured(roles = {"USER", "ADMIN"})
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

        Response error = errorCheck(user, note);

        if (error != null) {
            return error;
        }

        updateNote(title, contents, note);
        return Response.status(204).build();
    }
    
    private void updateNote(String title, String contents, Note note) {
        if (title != null) {
            note.setTitle(title);
        }

        if (contents != null) {
            note.setContents(contents);
        }

        dao.saveOrUpdate(note);
    }

    private Response errorCheck(User user, Note note) {
        if (note == null) {
            return Response.status(404)
                    .entity("Error 404: Note not found")
                    .build();
        }

        if (user.getId() != note.getUser().getId()
                && !securityContext.isUserInRole("ADMIN")) {

            return Response.status(403)
                    .entity("Error 403: Note does not belong to user")
                    .build();
        }

        return null;
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
