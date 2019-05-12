package com.blakesinner.quickNotes.api;

import javax.ws.rs.*;

@Path("/hello")
public class HelloWorld {

    @GET
    public String ping() {
        return "Hello World from Jersey!";
    }
}