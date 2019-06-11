package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.Note;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;

import javax.ws.rs.*;
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
    private final static int MAX_HEADER = 32768;

    /**
     * Save passed in note contents and title to database.
     *
     * @param id       the id
     * @param title    the title, can be null
     * @param contents the contents, can be null
     * @return         response indicating update success
     */
    @POST
    public Response saveNote(
            @QueryParam("id") String id
            , @HeaderParam("note-title") String title
            , @HeaderParam("note-contents") String contents) {

        User user = getUser();
        Note note = getNote(id);

        Response error = errorSearch(user, note, contents);

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
            note.setContents(replaceNewLines(contents));
        }

        dao.saveOrUpdate(note);
    }

    /**
     * Check if the note exists, if the user is permitted to
     * update it, and if the note is within the max file size.
     *
     * @param user     the current user
     * @param note     the note to update
     * @param contents the note contents
     * @return         response indicating the error, or null
     *                 if no error was found
     */
    private Response errorSearch(User user, Note note, String contents) {
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

        if (contents.getBytes().length > MAX_HEADER) {
            return Response.status(431)
                    .entity("Error 431: Note must smaller than " + (MAX_HEADER / 1024) + "kb")
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
    private Note getNote(String id) { return dao.getById(id); }

    /**
     * Get the current user, doesn't return null values because the filter
     * classes check that the user is logged in.
     *
     * @return the user
     */
    private User getUser() {
        return new GenericDAO<>(User.class).getById(securityContext.getUserPrincipal().getName());
    }

    /**
     * Replace newlines written as "\\n" with "\\\\n" because the database
     * converts "\\n" into "\n" and javascript needs two escape characters.
     *
     * @param contents the String to edit
     * @return         the edited String
     */
    private String replaceNewLines(String contents) {
        return contents.replaceAll("\\n", "\\\\n");
    }

}
