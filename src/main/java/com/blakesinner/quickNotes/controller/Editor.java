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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@WebServlet(
        urlPatterns = ("/editor")
)

public class Editor extends HttpServlet {

    /** Forwards to the editor page and passes it an note if a note id was included in the query string.
     *
     * @param req               The HttpRequest
     * @param res               The HttpResponse
     * @throws ServletException If a servlet exception occurs
     * @throws IOException      If an I/O exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        GenericDAO<Note> dao = new GenericDAO<>(Note.class);
//        String qString = req.getQueryString();
//
//
//        if (qString != null) {
//            Map<String, String> querys = parseQueryStrings(qString);
//
//            String contents = dao.getByPropertyEqual("id", querys.get("note")).get(0).getContents();
//
//            req.setAttribute("contents", contents);
//        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("/editor.jsp");
        dispatcher.forward(req, res);
    }

    /**
     * Returns a map of the query string parameters.
     *
     * @param queryString String containing query string
     * @return Map of query string
     */
    private Map<String, String> parseQueryStrings(String queryString) {
        String[] strings = queryString.split("&");

        if (strings.length < 1) return Collections.EMPTY_MAP;

        Map<String, String> results = new HashMap<>();

        for (int i = 0, l = strings.length; i < l; i++) {
            String[] temp = strings[i].split("=");

            if (temp.length <= 1) {
                continue;
            }

            results.put(temp[0], temp[1]);
        }
//        for (String str : strings) {
//            String[] temp = str.split("=");
//
//            if (temp.length > 1) {
//                results.put(temp[0], temp[1]);
//            } else {
//                results.put(temp[0], "");
//            }
//        }

        return results;
    }
}