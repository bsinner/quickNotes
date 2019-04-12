package com.blakesinner.quickNotes.api;

import javax.ws.rs.*;

@Secured(roles = {"USER", "ADMIN"})
@Path("/helloworld")
public class HelloWorld {

    @GET
    public String ping() {
        return "Hello World from Jersey!";
    }
}