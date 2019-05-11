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

/**
 * Endpoint to update a notes title and/or contents.
 *
 * @author bsinner
 */
@Secured(roles = {"USER", "ADMIN"})
@Path("/saveNote")
public class SaveNote {

    @Context
    private SecurityContext securityContext;
    private final static GenericDAO<Note> dao = new GenericDAO<>(Note.class);

    /**
     * Save passed in note contents and title to database.
     *
     * @param id       the id
     * @param title    the title, can be null
     * @param contents the contents, can be null
     * @return         response indicating update success
     */
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

    /**
     * Save changes of a passed in note to the database.
     *
     * @param title    the updated title
     * @param contents the updated contents
     * @param note     the note to update
     */
    private void updateNote(String title, String contents, Note note) {
        if (title != null) {
            note.setTitle(title);
        }

        if (contents != null) {
            note.setContents(contents);
        }

        dao.saveOrUpdate(note);
    }

    /**
     * Check if the note exists and if the user is permitted to
     * update it.
     *
     * @param user the current user
     * @param note the note to update
     * @return     response indicating the error, or null if no error
     *             was found
     */
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

    /**
     * Get the note to update.
     *
     * @param id the note id
     * @return   the note, or null if no note is found
     */
    private Note getNote(String id) {
        List<Note> notes = dao.getByPropertyEqual("id", id);
        if (notes.size() > 0) return notes.get(0);
        return null;
    }

    /**
     * Get the current user, doesn't return null values because the filter
     * classes check that the user is logged in.
     *
     * @return the user
     */
    private User getUser() {
        return new GenericDAO<>(User.class)
                .getByPropertyEqual(
                        "id"
                        , securityContext.getUserPrincipal().getName())
                .get(0);
    }

}
