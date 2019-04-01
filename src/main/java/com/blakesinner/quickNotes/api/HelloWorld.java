package com.blakesinner.quickNotes.api;

import javax.ws.rs.*;

@Path("/helloworld")
public class HelloWorld {

    @GET
    public String ping() {
        return "Hello World from Jersey!";
    }
}