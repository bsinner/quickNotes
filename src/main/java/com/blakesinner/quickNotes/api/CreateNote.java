package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.Note;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Endpoint to create note
 *
 * @author bsinner
 */
@Secured(roles = {"USER", "ADMIN"})
@Path("/create")
public class CreateNote {

    @Context
    private SecurityContext securityContext;
    private final static GenericDAO<Note> dao = new GenericDAO<>(Note.class);

    /**
     * Create note.
     *
     * @param title the note title
     * @return      response indicating success of note creation
     */
    @PUT
    public Response createNote(@QueryParam("title") String title) {

        if (duplicateCheck(title)) {
            return duplicateResponse(title);
        }

        Note note = getNewNote(title);

        return note != null ? okResponse(note.getId()) : serverErrorResponse();
    }

    /**
     * Check if the current user already has a note with the same title
     *
     * @param title the note title
     * @return      true if a duplicate is found, false otherwise
     */
    private boolean duplicateCheck(String title) {
        Set<Note> allNotes = getCurrentUser().getNotes();

        List<Note> userNotes = allNotes.stream()
                .filter(note -> note.getTitle().equals(title))
                .collect(Collectors.toCollection(ArrayList::new));

        return userNotes.size() != 0;
    }

    /**
     * Create a new note, add it to the database, and return the created note.
     *
     * @param title the note title
     * @return      the created note
     */
    private Note getNewNote(String title) {
        User user = getCurrentUser();
        Note note = new Note(title, "{}", user);
        int id = dao.insert(note);

        List<Note> newNotes = dao.getByPropertyEqual("id", String.valueOf(id));

        if (newNotes.size() == 0) {
            return null;
        }

        return newNotes.get(0);
    }

    /**
     * Response to send if the user has a note with the given title.
     *
     * @param title the title of the note to create
     * @return      the error response
     */
    private Response duplicateResponse(String title) {
        return Response.status(422)
                .entity("Error 422: Note with title " + title + " already exists")
                .build();
    }

    /**
     * Response to send if the server is unable to add a new note.
     *
     * @return the error response
     */
    private Response serverErrorResponse() {
        return Response.status(500).entity("Error 500: Note could not be created").build();
    }

    /**
     * Response to send if a note is created successfully.
     *
     * @param id the added note id
     * @return   Ok response with the added note id
     */
    private Response okResponse(int id) {
        return Response.status(200).entity(id).build();
    }

    /**
     * Get the user making the current request.
     *
     * @return the current user
     */
    private User getCurrentUser() {
        return new GenericDAO<>(User.class)
                .getByPropertyEqual(
                        "id"
                        , securityContext.getUserPrincipal().getName())
                .get(0);
    }

}
