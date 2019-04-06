package com.blakesinner.quickNotes.api;

import javax.ws.rs.*;
import static com.blakesinner.quickNotes.api.Role.*;

@Secured(
    roles = {USER, ADMIN},
    ownerOnlyRoles = {USER}
)
@Path("/helloworld")
public class HelloWorld {

    @GET
    public String ping() {
        return "Hello World from Jersey!";
    }
}