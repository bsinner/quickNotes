package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.util.ServletAuthenticator;
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
@WebServlet(urlPatterns = "/viewNotes")
public class ViewNotes extends HttpServlet {

    /**
     * Forward to viewNotes JSP, or login JSP if not logged in.
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
     * Forward to viewNotes JSP when login page makes post request to this servlet,
     * or forward to login JSP if not logged in.
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
     * Set up request dispatcher with ServletAuthenticator and forward to destination JSP.
     *
     * @param req               The HttpRequest
     * @param res               The HttpResponse
     * @throws IOException      If an I/O exception occurs
     * @throws ServletException If a servlet exception occurs
     */
    private void forwardToJsp(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        ServletAuthenticator auth = new ServletAuthenticator(req, res
                , (byte[]) getServletContext().getAttribute("authKey"));

        auth.setUpDispatcher("/viewNotes.jsp", "/viewNotes")
                .forward(req, res);
    }
}
