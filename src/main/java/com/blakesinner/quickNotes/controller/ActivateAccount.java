package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.util.ServletAuthenticator;

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
        ServletAuthenticator auth = new ServletAuthenticator(req, res);
        req.setAttribute("params", req.getQueryString());

        auth.setUpDispatcher("/activate.jsp"
                        , "/acivate" + (req.getQueryString() == null
                                ? ""
                                : "?" + req.getQueryString()))
                .forward(req, res);

    }

}
