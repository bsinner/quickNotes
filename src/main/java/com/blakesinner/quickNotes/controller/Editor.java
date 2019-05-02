package com.blakesinner.quickNotes.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(
        urlPatterns = ("/editor")
)

public class Editor extends HttpServlet {

    private static final Pattern PATTERN = Pattern.compile("note=[0-9]+");
    private static final String PARAM = "note=";

    /** Forwards to the editor page and passes it an note if a note id was included in the query string.
     *
     * @param req               The HttpRequest
     * @param res               The HttpResponse
     * @throws ServletException If a servlet exception occurs
     * @throws IOException      If an I/O exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/editor.jsp");
        dispatcher.forward(req, res);
    }

}