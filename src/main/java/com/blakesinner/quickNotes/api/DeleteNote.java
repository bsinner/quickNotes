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

@Secured(roles = {"USER", "ADMIN"})
@Path("/delete")
public class DeleteNote {

    @Context
    private SecurityContext securityContext;
    private final static GenericDAO<Note> dao = new GenericDAO<>(Note.class);

    @DELETE
    public Response deleteNote(@QueryParam("id") String id) {
        List<Note> notes = dao.getByPropertyEqual("id", id);

        if (notes.size() == 0) {
            return Response.status(404).entity("Note with id " + id + " not found").build();
        }

        Note note = notes.get(0);
        Optional<Response> authResults = checkIfUnauthorized(note);

        return authResults.orElseGet(() -> deleteFromDatabase(note));

        // check if authorized

        // try to delete note, return 404 or 200
    }

    private Optional<Response> checkIfUnauthorized(Note note) {
        return Optional.of(Response.status(403).entity("Note to delete does not belong to current user").build());
    }

    private Response deleteFromDatabase(Note note) {
        return Response.status(404).build(); // placeholder
    }

}
