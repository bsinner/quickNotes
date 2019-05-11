package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.Note;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import com.blakesinner.quickNotes.util.DurationMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Set;

/**
 * Endpoints for getting one or all notes.
 *
 * @author bsinner
 */
@Secured(roles = {"USER", "ADMIN"})
@Path("/note")
public class GetNote {

    @Context
    private SecurityContext securityContext;
    private final static GenericDAO<Note> dao = new GenericDAO<>(Note.class);
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Get the contents of one note if the note belongs to the current user
     * or if the current user is an admin.
     *
     * @param id the note id
     * @return   the note contents
     */
    @GET
    @Produces({"application/json"})
    public Response getNote(@QueryParam("id") String id) {
        List<Note> notes = dao.getByPropertyEqual("id", id);
        int userId = Integer.parseInt(securityContext.getUserPrincipal().getName());

        if (notes.size() > 0) {

            if (notes.get(0).getUser().getId() != userId
                    && !securityContext.isUserInRole("ADMIN")) {

                return Response.status(403).build();

            }

            return Response.status(200).entity(notes.get(0).getContents()).build();
        }

        return Response.status(404).entity("Error 404 - Note Not Found").build();
    }

    /**
     * Get JSON list summarizing the current user's notes.
     *
     * @return list of user notes, with ids, titles, and dates
     */
    @GET
    @Path("/list")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllUserNotes() {

        GenericDAO<User> DAO = new GenericDAO<>(User.class);

        Set<Note> notes = DAO.getByPropertyEqual(
                "id", securityContext.getUserPrincipal().getName()
        ).get(0).getNotes();

        String results = notes.size() > 0 ? createNoteJson(notes) : "{}";

        return Response.status(200).entity(results).build();
    }

    /**
     * Creates note list json.
     *
     * @param notes the notes to represent in json
     * @return      the json object
     */
    private String createNoteJson(Set<Note> notes) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        for (Note note : notes) {
            ObjectNode childNode = mapper.createObjectNode();
            childNode.put("created", DurationMessage.elapsed(note.getCreationDate()));
            childNode.put("title", note.getTitle());
            root.set(String.valueOf(note.getId()), childNode);
        }

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (JsonProcessingException jpe) {
            logger.trace("Json processing exception occurred");
        }

        return "{}";
    }

}
