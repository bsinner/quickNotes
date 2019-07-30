package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.util.ServletAuthenticator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Forward to the account activation page.
 *
 * @author bsinner
 */
@WebServlet(urlPatterns = {"/activate"})
public class ActivateAccount extends HttpServlet {

    /**
     * Forward to account activation page, send query string in request attribute for JS.
     *
     * @param req               the HTTP Request
     * @param res               the HTTP Response
     * @throws IOException      if an I/O Exception occurs
     * @throws ServletException if a Servlet Exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        forwardToJsp(req, res);
    }

    /**
     * Forward to the account activation page when login.jsp makes a post request, send query string in
     * request attribute for JS.
     *
     * @param req               the HTTP Request
     * @param res               the HTTP Response
     * @throws IOException      if an I/O Exception occurs
     * @throws ServletException if a Servlet Exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        forwardToJsp(req, res);
    }

    /**
     * Set the request dispatcher's destination JSP with ServletAuthenticator, add query string to request attribute,
     * forward to the destination JSP.
     *
     * @param req               the HTTP Request
     * @param res               the HTTP Response
     * @throws IOException      if an I/O Exception occurs
     * @throws ServletException if a Servlet Exception occurs
     */
    private void forwardToJsp(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        ServletAuthenticator auth = new ServletAuthenticator(req, res
                , (byte[]) getServletContext().getAttribute("authKey"));

        req.setAttribute("params", req.getQueryString());

        auth.setUpDispatcher("/activate.jsp"
                , "/activate" + (req.getQueryString() == null
                        ? ""
                        : "?" + req.getQueryString()))
                .forward(req, res);
    }

}
