package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.util.JspFilter;

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
        JspFilter filter = new JspFilter(req.getCookies());

        boolean test = filter.isValid();

        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsps/login.jsp");
        rd.forward(req, res);
    }

}
