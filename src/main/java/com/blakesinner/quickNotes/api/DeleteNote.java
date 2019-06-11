package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.Note;
import com.blakesinner.quickNotes.persistence.GenericDAO;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Optional;

/**
 * API Endpoint to delete note.
 *
 * @author bsinner
 */
@Secured(roles = {"USER", "ADMIN"})
@Path("/delete")
public class DeleteNote {

    @Context
    private SecurityContext securityContext;
    private final static GenericDAO<Note> dao = new GenericDAO<>(Note.class);

    /**
     * Search for and delete note.
     *
     * @param id the note id
     * @return   response indicating status of note deletion
     */
    @DELETE
    public Response deleteNote(@QueryParam("id") String id) {
        Note note = dao.getById(id);

        if (note == null) {
            return Response.status(404).entity("Note ID: " + id + " not found").build();
        }

        return checkIfUnauthorized(note)
                .orElseGet(() -> deleteFromDatabase(note));
    }

    /**
     * Check if the current user owns the note to delete, or if the current user
     * has admin privileges.
     *
     * @param note the note to delete
     * @return     forbidden response or empty Optional object
     */
    private Optional<Response> checkIfUnauthorized(Note note) {
        if (note.getUser().getId() != Integer.parseInt(securityContext.getUserPrincipal().getName())
                && !securityContext.isUserInRole("ADMIN")
        ) {
            return Optional.of(
                    Response.status(403)
                    .entity("Note ID: " + note.getId() + " does not belong to current user")
                    .build()
            );
        }

        return Optional.empty();

    }

    /**
     * Delete the note from the database.
     *
     * @param note the note to delete
     * @return     response indicating success of note deletion
     */
    private Response deleteFromDatabase(Note note) {
        dao.delete(note);
        Note noteSearch = dao.getById(note.getId());

        return noteSearch == null
                ? Response.status(200)
                        .entity("Note ID: " + note.getId() + " deleted")
                        .build()
                : Response.status(500)
                        .entity("Note ID: " + note.getId() + " found, but could not be deleted")
                        .build();

    }

}
