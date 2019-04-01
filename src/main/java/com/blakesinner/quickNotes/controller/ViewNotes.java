package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to view all notes.
 *
 * @author bsinner
 */
@WebServlet(
        urlPatterns = ("/viewNotes")
)

public class ViewNotes extends HttpServlet {

    /**
     * Get user notes and forward to viewNotes jsp page.
     *
     * @param req               The HttpRequest
     * @param res               The HttpResponse
     * @throws ServletException If a servlet exception occurs
     * @throws IOException      If an I/O exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        User user = new GenericDAO<>(User.class).getByPropertyEqual("id", "3").get(0);

        req.setAttribute("notes", user.getNotes());

        RequestDispatcher rd = req.getRequestDispatcher("/viewNotes.jsp");
        rd.forward(req, res);
    }

}
