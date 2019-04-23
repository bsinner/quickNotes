package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.Note;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Set;

@Secured(roles = {"USER", "ADMIN"})
@Path("/note")
public class GetNote {

    @Context
    private SecurityContext securityContext;
    private final static GenericDAO<Note> dao = new GenericDAO<>(Note.class);
    private final Logger logger = LogManager.getLogger(this.getClass());

    @GET
    public Response getNote(@QueryParam("id") String id) {
        List<Note> notes = dao.getByPropertyEqual("id", id);
        int userId = Integer.parseInt(securityContext.getUserPrincipal().getName());

        if (notes.size() > 0) {
            if (notes.get(0).getUser().getId() != userId) {
                return Response.status(403).build();
            }

            return Response.status(200).entity(notes.get(0).getContents()).build();
        }

        return Response.status(404).entity("Error 404 - Note Not Found").build();
    }

    /**
     * Get JSON object of user note titles, dates, and ids.
     * TODO: date created, last modified, or both?
     * @return
     */
    @GET
    @Path("/all")
    public Response getAllUserNotes() {

        GenericDAO<User> DAO = new GenericDAO<>(User.class);

        Set<Note> notes = DAO.getByPropertyEqual(
                "id", securityContext.getUserPrincipal().getName()
        ).get(0).getNotes();

        String results = notes.size() > 0 ? createNoteJson(notes) : "{}";

        return Response.status(200).entity(results).build();
    }


    private String createNoteJson(Set<Note> notes) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.createObjectNode();

        for (Note note : notes) {
            JsonNode childNode = mapper.createObjectNode();
            ((ObjectNode) childNode).put("created", note.getCreationDate().toString());
            ((ObjectNode) childNode).put("title", note.getTitle());
            ((ObjectNode) root).set("note" + note.getId(), childNode);
        }
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (JsonProcessingException jpe) {
            logger.trace("Json processing exception occurred");
        }

        return "{}";
    }

}
