package com.blakesinner.quickNotes.api;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//    https://docs.microsoft.com/en-us/azure/cognitive-services/translator/reference/v3-0-translate?tabs=curl#request-body
@Path("/translate")
public class Translate {

    private static final String BASE_URL = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0"


    @GET
    public Response translateNoteText(
            @HeaderParam("translate-text") String text
            , @QueryParam("from") String from
            , @QueryParam("to") String to) {

//        [{"Text":"Hello "},{"Text":"World"}]
        Client client = ClientBuilder.newClient();

        Response response = client
                .target("http://localhost:8080/quickNotes_war/")
                .path("api/hello")
                .request(MediaType.TEXT_PLAIN)
                .get(Response.class);


        return response;
    }

}
