package com.blakesinner.quickNotes.controller;

import javax.servlet.RequestDispatcher;
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
@WebServlet(urlPatterns = { "/activate" }, name = "activate")
public class ActivateAccount extends HttpServlet {

    /**
     * Forward to account activation page.
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
     * Forward to the account activation page when login.jsp makes a post request.
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
     * Forward to activate JSP, add query string to request attribute for JS.
     *
     * @param req               the HTTP Request
     * @param res               the HTTP Response
     * @throws IOException      if an I/O Exception occurs
     * @throws ServletException if a Servlet Exception occurs
     */
    private void forwardToJsp(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        req.setAttribute("params", req.getQueryString());

        RequestDispatcher rd = req.getRequestDispatcher("/activate.jsp");

        rd.forward(req, res);
    }

}
