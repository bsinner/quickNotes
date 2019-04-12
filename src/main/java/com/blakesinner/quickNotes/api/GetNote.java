package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.Note;
import com.blakesinner.quickNotes.persistence.GenericDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Secured(roles = {"ADMIN"})
@Path("/note")
public class GetNote {

    @GET
    @OwnerOnly(
            resourceType = Note.class
            , fkParam = "user_id"
            , roles = {"USER"}
    )
    public String getNote(@QueryParam("id")String id) {
        GenericDAO<Note> dao = new GenericDAO<>(Note.class);

        return dao.getByPropertyEqual("id", id).get(0).getContents();
    }
}
