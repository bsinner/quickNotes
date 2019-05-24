package com.blakesinner.quickNotes.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Forward to the account activating page.
 *
 * @author bsinner
 */
@WebServlet(urlPatterns = {"/activate"})
public class ActivateAccount extends HttpServlet {

    /**
     * Forward to the account activation page, send the query string in an request
     * attribute so it can be used by JavaScript.
     *
     * @param req               the HTTP Request
     * @param res               the HTTP Response
     * @throws IOException      if an I/O Exception occurs
     * @throws ServletException if a Servlet Exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        req.setAttribute("params", req.getQueryString());
        req.getRequestDispatcher("/WEB-INF/jsps/activate.jsp")
                .forward(req, res);
    }

}
