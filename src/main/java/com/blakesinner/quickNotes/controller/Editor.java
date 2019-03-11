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
import java.util.HashMap;
import java.util.Map;

@WebServlet(
        urlPatterns = ("/editor")
)

public class Editor extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        GenericDAO<Note> dao = new GenericDAO<>(Note.class);
        String qString = req.getQueryString();


        if (qString != null) {
            Map<String, String> querys = parseQueryStrings(qString);

            String contents = dao.getByPropertyEqual("id", querys.get("note")).get(0).getContents();

            req.setAttribute("contents", contents);
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("/editor.jsp");
        dispatcher.forward(req, res);
    }

    private Map<String, String> parseQueryStrings(String queryString) {
        String[] strings = queryString.split("&");
        Map<String, String> results = new HashMap<>();

        for (String str : strings) {
            String[] temp = str.split("=");
            results.put(temp[0], temp[1]);
        }

        return results;
    }
}