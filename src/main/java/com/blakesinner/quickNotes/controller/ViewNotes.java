package com.blakesinner.quickNotes.controller;

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
@WebServlet(urlPatterns = "/viewNotes", name = "viewNotes")
public class ViewNotes extends HttpServlet {

    /**
     * Forward to viewNotes JSP.
     *
     * @param req               The HttpRequest
     * @param res               The HttpResponse
     * @throws IOException      If an I/O exception occurs
     * @throws ServletException If a servlet exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        forwardToJsp(req, res);
    }

    /**
     * Forward to viewNotes JSP when login page makes post request.
     *
     * @param req               The HttpRequest
     * @param res               The HttpResponse
     * @throws IOException      If an I/O exception occurs
     * @throws ServletException If a servlet exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        forwardToJsp(req, res);
    }

    /**
     * Forward to viewNotes JSP.
     *
     * @param req               The HttpRequest
     * @param res               The HttpResponse
     * @throws IOException      If an I/O exception occurs
     * @throws ServletException If a servlet exception occurs
     */
    private void forwardToJsp(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        RequestDispatcher rd = req.getRequestDispatcher("/viewNotes.jsp");
        rd.forward(req, res);
    }
}
