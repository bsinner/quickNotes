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

@WebServlet(
        urlPatterns = ("/viewNotes")
)

public class ViewNotes extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        GenericDAO<Note> dao = new GenericDAO<>(Note.class);
        List<Note> notes = dao.getByPropertyEqual("userId", "3");

        req.setAttribute("notes", notes);

        RequestDispatcher rd = req.getRequestDispatcher("/viewNotes.jsp");
        rd.forward(req, res);
    }

}
