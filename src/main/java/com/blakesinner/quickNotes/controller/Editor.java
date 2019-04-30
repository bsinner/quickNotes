package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.entity.Note;
import com.blakesinner.quickNotes.persistence.GenericDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
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
//        String qString = req.getQueryString();
//
//        if (qString != null) {
//            String contents = getContents(qString);
//            if (contents != null) {
//                req.setAttribute("contents", contents);
//            }
//        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("/editor.jsp");
        dispatcher.forward(req, res);
    }

    private String getContents(String qString) {
        String id = parseId(qString);

        if (id != null) {
            GenericDAO<Note> dao = new GenericDAO<>(Note.class);
            List<Note> results = dao.getByPropertyEqual("id", id);

            if (results.size() > 0) {
                return results.get(0).getContents();
            }
        }

        return null;
    }

    private String parseId(String qString) {
        Matcher matcher = PATTERN.matcher(qString);

        if (matcher.find()) {
            return matcher.group(0).replace(PARAM, "");
        }

        return null;
    }

}