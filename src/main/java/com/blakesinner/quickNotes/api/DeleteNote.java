package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.Note;
import com.blakesinner.quickNotes.persistence.GenericDAO;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Optional;

@Secured(roles = {"USER", "ADMIN"})
@Path("/delete")
public class DeleteNote {

    @Context
    private SecurityContext securityContext;
    private final static GenericDAO<Note> dao = new GenericDAO<>(Note.class);

    @DELETE
    public Response deleteNote(@QueryParam("id") String id) {
        // check if authorized

        // try to delete note, return 404 or 200
    }

    private Optional<Response> checkIfAuthorized() {
    }

    private Response deleteFromDatabase() {
    }

    private Response buildForbidden() {

    }

    private Response buildNotFound() {

    }

    private Response buildOK() {
    }
}
