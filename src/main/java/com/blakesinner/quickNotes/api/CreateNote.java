package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.Note;
import com.blakesinner.quickNotes.persistence.GenericDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Secured(roles = {"USER", "ADMIN"})
@Path("/create")
public class CreateNote {

    @Context
    private SecurityContext securityContext;
    private final static GenericDAO<Note> dao = new GenericDAO<>(Note.class);

    // TODO: change to put
    @GET
    public Response createNote(
            @QueryParam("id") String id
            , @QueryParam("title") String title) {

        if (duplicateCheck(title)) {
            return duplicateResponse(title);
        }

        return Response.ok().build();
    }

    private boolean duplicateCheck(String title) {
        List<Note> notes = dao.getByPropertyEqual("title", title);

        return notes.size() == 0;
    }

    private Response duplicateResponse(String title) {
        return Response.status(422)
                .entity("Error 422: Note with title " + title + " already exists")
                .build();
    }

}
