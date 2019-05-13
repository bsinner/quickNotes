package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.util.KeyLoader;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Wrapper class for calling Microsoft Translate api, inputs must match those
 * of the Translate api. Requests must have less than 5000 characters of text to
 * translate, and have less than 100 "Text" objects.
 *
 * Request body format:
 * https://docs.microsoft.com/en-us/azure/cognitive-services/translator/reference/v3-0-translate?tabs=curl#request-body
 *
 * Query string language abbreviations:
 * https://api.cognitive.microsofttranslator.com/languages?api-version=3.0
 *
 * @author bsinner
 */
@Path("/translate")
public class Translate {

    private static final String BASE_URL = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0";
    private static final String AUTH_HEADER = "Ocp-Apim-Subscription-Key";
    private static final String KEY = "/translateKey.txt";

    /**
     * Get translation of passed in text
     *
     * @param text the text in the request body
     * @param from the source language abbreviation
     * @param to   the destination abbreviation
     * @return     the translated text, in json
     */
    @POST
    public Response translateNoteText(String text
            , @QueryParam("from") String from
            , @QueryParam("to") String to) {

        Client client = ClientBuilder.newClient();

        return client
                .target(BASE_URL + "&from=" + from + "&to=" + to)
                .request(MediaType.APPLICATION_JSON)
                .header(AUTH_HEADER, new KeyLoader().getKey(KEY))
                .post(Entity.json(text));
    }


}
